package com.zoaramas.optimization.logic;

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.zoaramas.optimization.logic.FonctionPack.Contrainte;

public class Systeme {
    private ArrayList<Variable> listeVariable;
    private Fonction fonctionObjectif;
    private ArrayList<Contrainte> listeContrainte;
    private ArrayList<Contrainte> listeContrainteSigne;

    private ArrayList<Fraction[][]> historique;
    private ArrayList<String> historiqueBase;
    private ArrayList<ArrayList<Variable>> historiqueListeVariable;

    public Systeme(ArrayList<Variable> listeVariable, Fonction fonctionObjectif, ArrayList<Contrainte> listeContrainte,
            ArrayList<Contrainte> listeContrainteSigne) throws Exception {
        this.listeVariable = listeVariable;
        this.fonctionObjectif = fonctionObjectif;
        this.listeContrainte = listeContrainte;
        this.listeContrainteSigne = listeContrainteSigne;

        this.historique = new ArrayList<>();
        this.historiqueListeVariable = new ArrayList<>();
        this.historiqueBase = new ArrayList<>();
        if (!verifierIntegriteVariableDesFonctions())
            throw new Exception("La verificatoin de l'integrite des variables de la fonction a echoue");
    }
    
    public Result simplexerMaxMin(boolean maximiser) throws Exception {
        if (!maximiser)
            negativerFonctionObjective();
        // System.out.println("fonction objectif negative");
        this.str();
        // Ici si on minimise, on passe en maximisation
        // Si jamais il y a erreur de degenerescence,
        Result result = (this.simplexer(maximiser)); // Maximiser
        if (!maximiser)
            negativerFonctionObjective();
        return result;
    }

    // Multiplier par -1 toutes les coeficients objectifs
    public void negativerFonctionObjective() {
        this.fonctionObjectif.negativerFonction();
    }

    public Result simplexer(boolean maximiser) throws Exception {
        Systeme nouveauSystemeTest = getNouveauSysteme();
        boolean test = nouveauSystemeTest.necessite2Etapes();
        System.out.println("test simplex initiation");
        int changementPhase = -1;
        if (test) {
            System.out.println("Resolution en 2 etapes!");
            // Obtenir le tableau simplex avec les variables artificielles
            TableSimplex ts = getTableauSimplex2();
            HashMap<Variable, Fraction> result = (ts.algo(true)).getListe();
            Util.appendArrayListOfArrays(historique, ts.getHistorique());
            Util.appendArrayListOfArraysString(historiqueBase, ts.getHistoriqueBase());
            this.historiqueListeVariable.add(ts.getSysteme().getListeVariable());
            changementPhase = this.historique.size();
            System.out.println("On a le resultat de la premiere phase juste en dessus");
            // Si une variable artificielle apparait encore dans la base, il n'y a pas de solution
            if(ts.getBase().contientArtificielle()) throw new Exception("Solution non trouve, existence de variable artificielle dans la base " + ts.getBase().disp());

            // Ici, on a deja la nouvelle base ainsi que les valeurs resultantes
            TableSimplex tsOriginal = getTableauSimplex();
            tsOriginal.setBase(ts.getBase());
            // On va devor modifier la table et la base de cette table simplex original
            // Mais obtenir d'abord la derniere ligne objective avec les valeurs de la base.
            Fraction[] derniereLigneOriginale = this.fonctionObjectif.getDerniereLigneFonctionTableSimplexFraction(
                    Variable.convertListToArray(tsOriginal.getSysteme().getListeVariable()), result);
            Fraction[][] newTable = TableSimplex.enleverArtifices(ts, derniereLigneOriginale);
            for (int i = 0; i < newTable.length; i++) {
                Fraction[] tempTemp = newTable[i];
                for (int j = 0; j < tempTemp.length; j++) {
                    System.out.print(tempTemp[j] + ", ");
                }
                System.out.println("\n");
            }
            tsOriginal.setTable(newTable);
            // System.out.println("\n\nFNALEMENT = \n");
            tsOriginal.setLigneObjectiveExprime2Phase();
            // System.out.println(tsOriginal.disp());
            // System.out.println("Avant d'ajouter = " + tsOriginal.getBase().disp());
            // On vient de set donc maintenant on peu tout simplement ajouter la premiere table corrige et suprimer la derniere qui ne marche pas encore
            // this.historiqueBase.remove(historiqueBase.size() - 1);
            tsOriginal.getHistoriqueBase().set(0, tsOriginal.getBase().disp() + "-");
            tsOriginal.getHistorique().set(0, Service.clone(tsOriginal.getTable()));
            // this.historiqueBase.add(tsOriginal.getBase().disp() + "-");
            Result finalResult = tsOriginal.algo(maximiser);
            Util.appendArrayListOfArrays(historique, tsOriginal.getHistorique());
            Util.appendArrayListOfArraysString(historiqueBase, tsOriginal.getHistoriqueBase());
            this.historiqueListeVariable.add(tsOriginal.getSysteme().getListeVariable());
            finalResult.setHistorique(historique);
            finalResult.setHistoriqueVariable(historiqueListeVariable);
            finalResult.setHistoriqueBase(historiqueBase);
            finalResult.setIndexChangementPhase(changementPhase);
            return finalResult;
        } else {
            System.out.println("Resolution en 1 seule etape! La resolution en 2 etapes n'est pas necessaire");
            TableSimplex ts = this.getTableauSimplex();
            Result finalResult = ts.algo(maximiser);
            this.historique = ts.getHistorique();
            this.historiqueListeVariable.add(ts.getSysteme().getListeVariable());
            finalResult.setHistorique(historique);
            finalResult.setHistoriqueVariable(historiqueListeVariable);
            finalResult.setIndexChangementPhase(changementPhase);
            finalResult.setHistoriqueBase(ts.getHistoriqueBase());
            return finalResult;
        }
    }

    // Clone le systeme
    public Systeme cloneSysteme() throws Exception{
        ArrayList<Variable> nouvelleListeVariables = Variable.cloneListVariable(this.listeVariable);
        Fonction nouvelleFonctionObjectif = this.fonctionObjectif.clone();
        ArrayList<Contrainte> nouveleListeContrainte = Contrainte.clonerListeContrainte(this.listeContrainte);
        ArrayList<Contrainte> nouveleListeContrainteSigne = Contrainte.clonerListeContrainte(this.listeContrainteSigne);
        
        Systeme result = new Systeme(nouvelleListeVariables, nouvelleFonctionObjectif, nouveleListeContrainte,
                nouveleListeContrainteSigne);
        return result;
    }
    // 2 PHASES
    // Obtenir la nouvelle base

    public int getNombreVariableArtificielle() {
        ArrayList<Variable> liste = Variable.getListeVariable(this.listeVariable, TypeVariable.artificielle);
        return liste.size();
    }

    // Obtenir le premier tableau simplex
    public TableSimplex getTableauSimplex2() throws Exception {
        // Ici, on ajoute les variables d'ecart d'abord, ensuite on a les variables
        // artificielles
        // Le nouveau systeme a utiliser lors de la deuxieme etape
        // Systeme nouveauSysteme = getNouveauSysteme();
        Systeme systemeArtificiel = getNouveauSysteme();
        systemeArtificiel.rajouterVariableArtificielle();
        systemeArtificiel.setListeContrainteSigne(systemeArtificiel
                .getNouvelleListeContrainteSigne(systemeArtificiel.getListeVariable(), TypeVariable.artificielle));
        systemeArtificiel.str();
        // float[] fArtificielle = systemeArtificiel.fArtificielle();
        Base b = getInititBase2Etape(systemeArtificiel);
        int[] indexPivot = { -1, -1 };
        // System.out.println(b.disp());
        // System.out.println("end");
        TableSimplex result = new TableSimplex(systemeArtificiel, b,
                systemeArtificiel.getContenuTableSimplex2PhasesFraction(b),
                indexPivot);
        System.out.println("resultat getTableauSimplex2");
        System.out.println(result.disp());
        return result;
    }

    public float[][] getContenuTableSimplex2Phases(Base b) throws Exception {
        float[][] result = new float[this.listeContrainte.size() + 1][];
        // Puisqu'on reste dans la partie initialisation de l'algorithme simplex a une
        // seule etape, les lignes seront alors juste les contraintes simples
        Variable[] arrayVariable = Variable.convertListToArray(listeVariable);
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            float[] coeffVariableBase = contrainte.getAllCoeffVariable(arrayVariable);
            result[i] = coeffVariableBase;
        }

        // rajouter la derniere ligne de la fonction
        result[result.length - 1] = this.fArtificielle();
        return result;
    }

    public Fraction[][] getContenuTableSimplex2PhasesFraction(Base b) throws Exception {
        Fraction[][] result = new Fraction[this.listeContrainte.size() + 1][];
        // Puisqu'on reste dans la partie initialisation de l'algorithme simplex a une
        // seule etape, les lignes seront alors juste les contraintes simples
        Variable[] arrayVariable = Variable.convertListToArray(listeVariable);
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            Fraction[] coeffVariableBase = contrainte.getAllCoeffVariableFraction(arrayVariable);
            result[i] = coeffVariableBase;
        }

        // rajouter la derniere ligne de la fonction
        result[result.length - 1] = this.fArtificielleFraction();
        return result;
    }

    // Obtenir la base de debut pour le simplex a 1 etape
    public static Base getInititBase2Etape(Systeme systeme) throws Exception {
        // Il y a autant d'element dans la base que de contrainte.
        // On priorise les variables artificielles
        Variable[] variableBase = new Variable[systeme.getListeContrainte().size()];
        for (int i = 0; i < systeme.getListeContrainte().size(); i++) {
            Contrainte contrainte = systeme.getListeContrainte().get(i);
            variableBase[i] = contrainte.getPrioriteArtificielleApresEcart();
        }
        Base base = new Base(systeme.getListeContrainte().size(), variableBase, systeme.getListeVariable());
        return base;
    }
    // for(int i = 0; i < fArtificielle.length; i ++) {
    // if(i != fArtificielle.length - 1)
    // System.out.println(systemeArtificiel.getListeVariable().get(i).getNom() + " =
    // " + fArtificielle[i]);
    // else System.out.println("SM = " + fArtificielle[i]);
    // }

    // Pour la deuxieme phase, obtenir la liste de variable artificielle en priorite
    // avant les ecarts
    public Variable[] getListeVariableArtificielleEcart() {
        // Liste de variable ordonnee
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);

        }
        ArrayList<Variable> listeVariableEcart = Variable.getListeVariable(this.listeVariable, TypeVariable.ecart);
        Variable[] result = new Variable[listeVariableEcart.size()];
        for (int i = 0; i < listeVariableEcart.size(); i++) {
            result[i] = listeVariableEcart.get(i);
        }
        return result;
    }

    // - intialisationTableau2Phases(prendre les variables artificielles en priorite
    // et utiliser fArtificielle)
    // - fArtificielle()
    public float[] fArtificielle() {
        float[] result = new float[this.listeVariable.size() + 1];
        for (int i = 0; i < this.listeVariable.size(); i++) {
            Variable v = this.listeVariable.get(i);
            result[i] = getCoeffContrainteArtificiele(v);
        }
        // second membre
        result[result.length - 1] = getCoeffContrainteArtificiele();
        return result;
    }

    public Fraction[] fArtificielleFraction() throws Exception {
        Fraction[] result = new Fraction[this.listeVariable.size() + 1];
        for (int i = 0; i < this.listeVariable.size(); i++) {
            Variable v = this.listeVariable.get(i);
            result[i] = new Fraction(getCoeffContrainteArtificiele(v));
        }
        // second membre
        result[result.length - 1] = new Fraction(getCoeffContrainteArtificiele());
        return result;
    }

    // - Pour une variable donne, obtenir le fArtificielle de cette variable, c'est
    // a dire la somme des coefficients mis en negatifs pour la variable de donne
    // dans les contraintes presentants une variable artificielle
    public float getCoeffContrainteArtificiele(Variable v) {
        float result = 0;
        if (v.getType().equals(TypeVariable.artificielle))
            return result;
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            if (contrainte.isArtificielle()) {
                result += contrainte.getCoeffVariable(v);
            }
        }
        return result;
    }

    // Pour le second membre
    public float getCoeffContrainteArtificiele() {
        float result = 0;
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            if (contrainte.isArtificielle()) {
                result += contrainte.getSecondMembre();
            }
        }
        return result;
    }

    // Rajouter une variable artificielle a chaque contrainte et dans la liste de
    // variable
    public void rajouterVariableArtificielle() throws Exception {
        // On doit rajouter une variable artificielle a chaque contrainte qui en en a
        // besoin
        int artificielIndex = 1;
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            boolean test = necessite2EtapesContrainte(contrainte);
            if (test) {
                Variable v = new Variable("A" + artificielIndex, TypeVariable.artificielle);
                artificielIndex++;
                contrainte.addVariableArtificielle(v);
                this.listeVariable.add(v);
            }
        }
    }

    public void str() {
        System.out.println("Liste de variable = ");
        Variable.printListVariable(listeVariable);
        System.out.println("F objectif: " + fonctionObjectif.str());
        System.out.println("Liste contraintes:");
        for (int i = 0; i < listeContrainte.size(); i++) {
            System.out.println(listeContrainte.get(i).str());
        }
        for (int i = 0; i < listeContrainteSigne.size(); i++) {
            System.out.println(listeContrainteSigne.get(i).str());
        }
    }

    // 1 PHASE
    // Obtenir le nombre de variable d'ecart selon les contraintes qui ont des
    // signes differents de egal
    public static int getNbrVariableEcart(Systeme systeme) {
        int count = 0;
        ArrayList<Contrainte> listeContrainte = systeme.getListeContrainte();
        for (int i = 0; i < systeme.getListeContrainte().size(); i++) {
            if (!listeContrainte.get(i).getSigne().equals(Signe.egal)) {
                count++;
            }
        }
        return count;
    }

    // Obtenir le premier tableau simplex
    public TableSimplex getTableauSimplex() throws Exception {
        Systeme nouveauSysteme = getNouveauSysteme();
        Base b = getInititBase1Etape(nouveauSysteme, Systeme.getNbrVariableEcart(this));
        int[] indexPivot = { -1, -1 };
        TableSimplex result = new TableSimplex(nouveauSysteme, b, nouveauSysteme.getContenuTableSimplexFraction(b),
                indexPivot);
        return result;
    }

    public Fraction[][] getContenuTableSimplexFraction(Base b) throws Exception {
        Fraction[][] result = new Fraction[this.listeContrainte.size() + 1][];
        // Puisqu'on reste dans la partie initialisation de l'algorithme simplex a une
        // seule etape, les lignes seront alors juste les contraintes simples
        Variable[] arrayVariable = Variable.convertListToArray(listeVariable);
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            Fraction[] coeffVariableBase = contrainte.getAllCoeffVariableFraction(arrayVariable);
            result[i] = coeffVariableBase;
        }

        // rajouter la derniere ligne de la fonction
        result[result.length - 1] = this.fonctionObjectif.getDerniereLigneFonctionTableSimplexFraction(arrayVariable);
        return result;
    }

    public float[][] getContenuTableSimplex(Base b) throws Exception {
        float[][] result = new float[this.listeContrainte.size() + 1][];
        // Puisqu'on reste dans la partie initialisation de l'algorithme simplex a une
        // seule etape, les lignes seront alors juste les contraintes simples
        Variable[] arrayVariable = Variable.convertListToArray(listeVariable);
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            float[] coeffVariableBase = contrainte.getAllCoeffVariable(arrayVariable);
            result[i] = coeffVariableBase;
        }

        // rajouter la derniere ligne de la fonction
        result[result.length - 1] = this.fonctionObjectif.getDerniereLigneFonctionTableSimplex(arrayVariable);
        return result;
    }

    // Obtenir la base de debut pour le simplex a 1 etape
    public Base getInititBase1Etape(Systeme systeme, int nombreContrainteNonEgalite) throws Exception {
        // Il y a autant d'element dans la base que de contrainte simple qui n'est pas
        // une egalite
        Base base = new Base(nombreContrainteNonEgalite, systeme.getListeVariableEcart(), systeme.getListeVariable());
        return base;
    }

    // Obtenir la liste de variable d'ecart
    public Variable[] getListeVariableEcart() {
        ArrayList<Variable> listeVariableEcart = Variable.getListeVariable(this.listeVariable, TypeVariable.ecart);
        Variable[] result = new Variable[listeVariableEcart.size()];
        for (int i = 0; i < listeVariableEcart.size(); i++) {
            result[i] = listeVariableEcart.get(i);
        }
        return result;
    }

    // Retourner le nouveaux systeme avec les variables d'ecart
    public Systeme getNouveauSysteme() throws Exception {
        ArrayList<Variable> nouvelleListeVariables = getNouvelleListeVariable();
        Fonction nouvelleFonctionObjectif = getNouvelleFonctionObjectif();
        ArrayList<Contrainte> nouveleListeContrainte = getNouvelleListeContrainte(nouvelleListeVariables);
        ArrayList<Contrainte> nouveleListeContrainteSigne = getNouvelleListeContrainteSigne(nouvelleListeVariables,
                TypeVariable.ecart);
        Systeme result = new Systeme(nouvelleListeVariables, nouvelleFonctionObjectif, nouveleListeContrainte,
                nouveleListeContrainteSigne);
        return result;
    }

    // Obtenir la nouvelle liste de contrainte de signe
    public ArrayList<Contrainte> getNouvelleListeContrainteSigne(ArrayList<Variable> nouvelleListeVariable,
            TypeVariable typeARajouter) {
        ArrayList<Contrainte> result = Contrainte.clonerListeContrainte(this.listeContrainteSigne);
        ArrayList<Variable> listeVariablesEcart = Variable.getListeVariable(nouvelleListeVariable, typeARajouter);
        // Creer les nouvelle contraintes de signes en addition pout les contraintes de
        // signes
        // Toutes les contraintes de signes doivent etre positif
        for (int i = 0; i < listeVariablesEcart.size(); i++) {
            Variable v = listeVariablesEcart.get(i);
            ArrayList<Terme> listeTermeContrainteSigne = new ArrayList<>();
            listeTermeContrainteSigne.add(new Terme(v, 1));
            float secondMembreSigne = 0;
            Contrainte contrainteSigne = new Contrainte(listeTermeContrainteSigne, Signe.superieurOuEgal,
                    secondMembreSigne);
            result.add(contrainteSigne);
        }
        return result;
    }

    // Obtenir la nouvelle liste de contrainte normale
    public ArrayList<Contrainte> getNouvelleListeContrainte(ArrayList<Variable> nouvelleListeVariable) {
        // La nouvelle liste de variable avec les variables d'ecart
        ArrayList<Variable> listeVariablesEcart = Variable.getListeVariable(nouvelleListeVariable, TypeVariable.ecart);
        ArrayList<Contrainte> result = new ArrayList<>();
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte temp = this.listeContrainte.get(i);
            // si le signe de contrainte est deja egal, pas besoin d'ajouter de variable
            // d'ecart
            Contrainte nouvelleContrainte = temp.modifierContrainte(Signe.egal);
            if (temp.getSigne() != Signe.egal) {
                nouvelleContrainte.ajouterVariableEcart(temp.getSigne(), listeVariablesEcart.get(0));
                listeVariablesEcart.remove(0);
            }
            result.add(nouvelleContrainte);
        }
        return result;
    }

    // Obtenir la nouvelle fonction objectif
    // La fonction objectif ne change pas lors du simplex a 1 etapte
    public Fonction getNouvelleFonctionObjectif() {
        return this.fonctionObjectif.clone();
    }

    // Retourner la nouvelle liste de variable
    // Puis on ajoute le nombre de variable d'ecart selon le nombre de contrainte
    public ArrayList<Variable> getNouvelleListeVariable() {
        // On ajouter des variables selon le nombre de contrainte normale
        ArrayList<Variable> result = Variable.cloneListVariable(this.listeVariable);
        for (int i = 1; i < this.listeContrainte.size() + 1; i++) {
            // On ajoute seulement si le signe n'est pas egal
            if (this.listeContrainte.get(i - 1).getSigne() != Signe.egal)
                result.add(new Variable("S" + i, TypeVariable.ecart));
        }
        return result;
    }

    // 1 PHASE
    // -> Retourne non si on peut utiliser qu'1 seule etape
    public boolean necessite2Etapes() throws Exception {
        // On est pas oblige d'utiliser 2 etapes quand toutes les contraintes peuvent
        // admettre x, y, z, etc = 0
        return (necessite2EtapesContraintesSignes() || necessite2EtapesContraintes());
    }

    // Verifier si les contraintes normales peuvent admettre les variables directs
    // initialise a 0 pour comencer
    public boolean necessite2EtapesContraintes() throws Exception {
        for (int i = 0; i < this.listeContrainte.size(); i++) {
            Contrainte contrainte = this.listeContrainte.get(i);
            boolean test = necessite2EtapesContrainte(contrainte);
            if (test)
                return true;
        }
        // On retourne false quand toutes les contraintes de signes on ete verifie true,
        // false veut dire on est pas oblige d'utiliser simplex avec 2 etapes
        return false;
    }

    public boolean necessite2EtapesContrainte(Contrainte contrainte) throws Exception {
        // On doit donner ici les valeurs des variables de bases qui sont tous a 0,
        // c'est a dire la liste de variable de la liste objectif
        HashMap<Variable, Float> mapValeurVariableObjectifAZero = getMapValeurAZeroFonctionObjectif();
        if (!contrainte.tester(mapValeurVariableObjectifAZero))
            return true;
        return false;

    }

    // Verifier si les contraintes de signes peuvent admettre les variables directs
    // initialise a 0 pour comencer
    public boolean necessite2EtapesContraintesSignes() throws Exception {
        for (int i = 0; i < this.listeContrainteSigne.size(); i++) {
            Contrainte contrainteSigne = this.listeContrainteSigne.get(i);
            boolean test = necessite2EtapesContrainteSigne(contrainteSigne);
            if (test)
                return true;
        }
        // On retourne false quand toutes les contraintes de signes on ete verifie true,
        // false veut dire on est pas oblige d'utiliser simplex avec 2 etapes
        return false;
    }

    public boolean necessite2EtapesContrainteSigne(Contrainte contrainteSigne) throws Exception {
        // Par convention, il n'y qu'une seule variable dans les contraintes de signes
        if (!contrainteSigne.tester(0))
            return true;
        return false;
    }

    // Obtenir le nombre de 0 relie au nombre de variable direct pour la fonction
    // objectif
    public HashMap<Variable, Float> getMapValeurAZeroFonctionObjectif() {
        return this.fonctionObjectif.getMapValeurAZero();
    }

    // Obtenir le nombre de 0 relie au nombre de variable direct pour la fonction
    // objectif
    public float[] getListeValeurAZeroFonctionObjectif() {
        return this.fonctionObjectif.getListeValeurAZero();
    }

    // Verifier les variables de chaque fonction dans le systeme
    public boolean verifierIntegriteVariableDesFonctions() throws Exception {
        boolean result = true;
        for (int i = 0; i < listeContrainte.size(); i++) {
            if (!listeContrainte.get(i).checkTermVariables(listeVariable)) {
                result = false;
                break;
            }
        }
        for (int i = 0; i < listeContrainteSigne.size(); i++) {
            if (!listeContrainteSigne.get(i).checkTermVariables(listeVariable)) {
                result = false;
                break;
            }
        }
        return result && fonctionObjectif.checkTermVariables(listeVariable);
    }

    public ArrayList<Variable> getListeVariable() {
        return listeVariable;
    }

    public void setListeVariable(ArrayList<Variable> listeVariable) {
        this.listeVariable = listeVariable;
    }

    public ArrayList<Contrainte> getListeContrainte() {
        return listeContrainte;
    }

    public void setListeContrainte(ArrayList<Contrainte> listeContrainte) {
        this.listeContrainte = listeContrainte;
    }

    public ArrayList<Contrainte> getListeContrainteSigne() {
        return listeContrainteSigne;
    }

    public void setListeContrainteSigne(ArrayList<Contrainte> listeContrainteSigne) {
        this.listeContrainteSigne = listeContrainteSigne;
    }

    public Fonction getFonctionObjectif() {
        return fonctionObjectif;
    }

    public void setFonctionObjectif(Fonction fonctionObjectif) {
        this.fonctionObjectif = fonctionObjectif;
    }

    public ArrayList<Fraction[][]> getHistorique() {
        return historique;
    }

    public void setHistorique(ArrayList<Fraction[][]> historique) {
        this.historique = historique;
    }

    public ArrayList<String> getHistoriqueBase() {
        return historiqueBase;
    }

    public void setHistoriqueBase(ArrayList<String> historiqueBase) {
        this.historiqueBase = historiqueBase;
    }

    public ArrayList<ArrayList<Variable>> getHistoriqueListeVariable() {
        return historiqueListeVariable;
    }

    public void setHistoriqueListeVariable(ArrayList<ArrayList<Variable>> historiqueListeVariable) {
        this.historiqueListeVariable = historiqueListeVariable;
    }

}
