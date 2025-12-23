package com.zoaramas.optimization.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSimplex {
    private Systeme systeme;
    private Base base;
    private Fraction[][] table;
    private int[] indicePivot; // LICO

    // Pour l'affichage
    private ArrayList<Fraction[][]> historique;
    private ArrayList<String> historiqueBase;

    public TableSimplex(Systeme systeme, Base base, Fraction[][] table, int[] indicePivot) {
        this.systeme = systeme;
        this.base = base;
        this.table = table;
        this.indicePivot = indicePivot;

        this.historique = new ArrayList<>(); 
        this.historique.add(Service.clone(table)); // On clone deja le premier
        this.historiqueBase = new ArrayList<>();
        historiqueBase.add(this.base.disp());
    }

    public String disp() {
        String result = this.base.disp();
        result += "\n";
        // Afficher l'entete
        result += "header: " + Variable.dispListVariable(this.systeme.getListeVariable());
        result += "\n";
        for (int i = 0; i < table.length; i++) {
            Fraction[] tableTemp = table[i];
            result += "[";
            for (int j = 0; j < tableTemp.length; j++) {
                result += tableTemp[j].str();
                if (j != tableTemp.length - 1)
                    result += ", ";
            }
            result += "]\n";
        }
        return result;
    }

    
    // 2eme phase
    // Obtenir la dernier ligne de la table lors de la 2eme phase
    // Cette fonction s'attend deja a la derniere ligne initialement tiree des
    // coefficients objectifs de base
    public void setLigneObjectiveExprime2Phase() throws Exception {
        // On va iterer les variables dans la base, et on va effectuer l'operation sur
        // la derniere ligne
        System.out.println("setLigneObjectiveExprime2Phase");
        System.out.println("base = ");
        System.out.println(this.base.disp());
        for (int i = 0; i < this.base.getListeVariable().length; i++) {
            int columnIndex = TableSimplex.getColonneVariable(this.base.getListeVariable()[i],
                    this.systeme.getListeVariable());
            Fraction[] temp = expressionObjectivePhase2(this.table[this.table.length - 1], this.table[i], columnIndex);
            this.table[this.table.length - 1] = temp;
            System.out.println("noms dans la liste de variable depuis la base = \n " + this.base.getListeVariable()[i].getNom());
        }
    }

    // Obtenir la colonne d'une variable dans le tableau
    public static int getColonneVariable(Variable v, ArrayList<Variable> listeVariable) throws Exception {
        for (int i = 0; i < listeVariable.size(); i++) {
            if (listeVariable.get(i).equals(v)) {
                return i;
            }
        }
        throw new Exception("La variable n'a pas ete trouve dans la table " + v.getNom());
    }

    // annuler une colonne d'une ligne par rapport a une autre ligne pivot, et
    // effectuer l'operation sur le reste de la ligne
    public Fraction[] expressionObjectivePhase2(Fraction[] ligne, Fraction[] lignePivot, int colonne) {
        Fraction coeff = Fraction.diviser(ligne[colonne], lignePivot[colonne]);
        // Fraction coeff = ligne[colonne] / lignePivot[colonne];
        Fraction[] result = new Fraction[ligne.length];
        // On modifie tout a part la derniere ligne
        for (int i = 0; i < ligne.length - 1; i++) {
            if (i == colonne) {
                result[i] = new Fraction();
            } else {
                Fraction mult = Fraction.multiplier(coeff, lignePivot[i]);
                result[i] = Fraction.soustraire(ligne[i], mult);
                // result[i] = ligne[i] - (coeff * lignePivot[i]);
            }
        }
        // Le second membre dernier ne change pas
        result[result.length - 1] = ligne[ligne.length - 1];
        return result;
    }

    public static Fraction[][] enleverArtifices(TableSimplex ts, Fraction[] ligneObjective) {
        Fraction[][] t = ts.getTable();
        // on enleve la derniere ligne mais en rajoute une ligne quand meme, le nombre
        // de colonne se voit diminuer du nombre de variable artificielle
        Fraction[][] result = new Fraction[t.length][];

        int nbLigne = result.length;
        int nbColonne = t[0].length;

        int nouvelleNombreColonne = t[0].length - ts.getSysteme().getNombreVariableArtificielle();

        ArrayList<Variable> listeVariable = ts.getSysteme().getListeVariable();
        for (int i = 0; i < nbLigne - 1; i++) {
            Fraction[] temp = t[i];
            Fraction[] tempResult = new Fraction[nouvelleNombreColonne];
            int index = 0;
            for (int j = 0; j < nbColonne; j++) {
                if (j < listeVariable.size() && listeVariable.get(j).getType().equals(TypeVariable.artificielle)) {
                } else {
                    tempResult[index] = temp[j];
                    index++;
                }
            }
            result[i] = tempResult;
            // System.out.println("temp = ");
            // for(int k = 0; k < result[i].length; k ++) {
            // System.out.println(result[i][k]);
            // }
        }
        // Pour la derniere ligne, on va devoir remplacer les valeurs de la base et
        // autres avec la fonction objective
        result[result.length - 1] = ligneObjective;
        return result;
    }

    // Obtenir carrement les resultats
    public HashMap<Variable, Fraction> getResultatSimplex(boolean maximisation) throws Exception {
        HashMap<Variable, Fraction> result = new HashMap<>();
        for (int i = 0; i < this.base.getTaille(); i++) {
            if (maximisation)
                result.put(this.getBase().getListeVariable()[i], this.table[i][this.table[i].length - 1]);
            else
                result.put(this.getBase().getListeVariable()[i], (this.table[i][this.table[i].length - 1]).mult(-1));
        }
        return result;
    }

    // Afficher resultat
    public void resultatSimplex() throws Exception {
        String result = "(";
        for (int i = 0; i < this.base.getTaille(); i++) {
            result += this.base.getListeVariable()[i].getNom() + " = " + this.table[i][this.table[i].length - 1];
            if (i != this.base.getTaille() - 1) {
                result += ", ";
            }
        }
        Fraction max = this.systeme.getFonctionObjectif().fFraction(getSecondMembres());
        System.out.println(result + "), f = " + max.str());
    }

    public Fraction getOptimum() throws Exception {
        Fraction max = this.systeme.getFonctionObjectif().fFraction(getSecondMembres());
        return max;
    }

    // Obtenir un array de Fraction[] a partir des seconds membres de la table simplex
    public HashMap<Variable, Fraction> getSecondMembres() {
        HashMap<Variable, Fraction> result = new HashMap<>();
        for (int i = 0; i < this.table.length - 1; i++) {
            this.base.getListeVariable()[i].afficher();
            result.put(this.base.getListeVariable()[i], this.table[i][this.table[i].length - 1]);
        }
        return result;
    }

    // Algorithme principal
    public Result algo(boolean maximiser) throws Exception {
        int count = 0;
        while (!stop()) {
            permuterTable();
            this.historique.add(Service.clone(table));
            this.historiqueBase.add(this.base.disp());
            count ++;
        }
        // Maintenant afficher le resultat
        // On va juste remplacer utiliser les variables de la base pour obtenir la
        // valeur max de fonction objective
        // String result = "max = ";
        // if(!maximiser) result = "min = ";
        // result = result + getOptimum();
        // System.out.println(result);
        // Fraction max = this.systeme.getFonctionObjectif().f(getSecondMembres());
        return new Result(getOptimum(), getResultatSimplex(maximiser), maximiser);
    }

    // Verifier si on doit arreter
    public boolean stop() {
        Fraction[] objectif = this.table[this.table.length - 1];
        for (int i = 0; i < objectif.length - 1; i++) {
            if (objectif[i].superieurStrictZero())
                return false;
        }
        return true;
    }

    public void annulerColonnePivot() throws Exception {
        for (int i = 0; i < this.table.length; i++) {
            if (i != indicePivot[0]) { // si la ligne n'est pas le pivot
                Fraction[] nouvelleTable = TableSimplex.annulerColonnePivotDeLigne(indicePivot[1],
                        this.table[indicePivot[0]], this.table[i]);
                this.table[i] = nouvelleTable;
            }
        }
    }

    // Rendre 0 l'element d'une ligne et effectuer l'operation sur le reste de
    // l'ensemble de la liste
    // la colonne du pivot ici est deja 1
    public static Fraction[] annulerColonnePivotDeLigne(int indiceColonnePivot, Fraction[] lignePivot, Fraction[] ligne)
            throws Exception {
        float epsilon = 0.000001f; // Define a small tolerance
        Fraction coeff = ligne[indiceColonnePivot];
        for (int i = 0; i < ligne.length; i++) {
            if (i == indiceColonnePivot)
                ligne[i] = new Fraction();
            else {
                Fraction mult = Fraction.multiplier(coeff, lignePivot[i]);
                ligne[i] = Fraction.soustraire(ligne[i], mult);
                if (Math.abs(ligne[1].getValue()) < epsilon) {
                    // throw new Exception("soustraction non supporte");
                    // ligne[i] = 0;
                }
            }
        }
        return ligne;
    }

    // Rendre 1 le pivot et effectuer l'ooperation sur le reste de la ligne
    // public
    // Permuter une variable dans la base
    public void permuterTable() throws Exception {
        System.out.println("permuterTable()");
        Variable rentrante = getVariableLePlusMaximisant();
        System.out.println("rentrante  = " + rentrante.getNom());
        Variable sortante = getVariableLeMoinsValeureuse(rentrante);
        setIndicePivot(rentrante, sortante);
        this.base.permuter(sortante, rentrante);
        // Rendre a 1 le pivot et effectuer l'operation sur le reste de la ligne
        Fraction diviseur = table[indicePivot[0]][indicePivot[1]];
        table[indicePivot[0]][indicePivot[1]] = new Fraction(1);
        // Effectuer l'operation correspondant au reste des elements de la ligne
        for (int i = 0; i < table[indicePivot[0]].length; i++) {
            if (i != indicePivot[1]) {
                Fraction temp = Fraction.diviser(table[indicePivot[0]][i], diviseur);
                // Fraction temp = table[indicePivot[0]][i] / diviseur;
                table[indicePivot[0]][i] = temp;
            }
        }

        // Annuler toute la colonne du pivot et effectuer les autres operations
        // necessaires
        annulerColonnePivot();
        System.out.println(this.disp());
    }

    public void setIndicePivot(Variable rentrante, Variable sortante) throws Exception {
        int indiceLigne = Variable.getIndexInArray(this.base.getListeVariable(), sortante);
        int indiceColonne = Variable.getIndexInList(this.systeme.getListeVariable(), rentrante);
        this.indicePivot[0] = indiceLigne;
        this.indicePivot[1] = indiceColonne;
    }

    // Trouver la variable dans la base qui est la moins valeureuse
    public Variable getVariableLeMoinsValeureuse(Variable maximisant) throws Exception {
        int indexMaximisant = Variable.getIndexInList(this.systeme.getListeVariable(), maximisant);
        // Verifier d'abord si au moins un variable de la base peut sortir de la base en
        // question:
        // Condition: la valeur correspondante a la colonne qui va s'introduire est > 0
        if (!auMoins1VariableBasePeutSortir(indexMaximisant)) {
            throw new Exception(
                    "L'algorithme ne peut plus continuer, aucune variable dans la base ne peut etre remplace");
        }
        int indexMoinsValeureux = -1;
        int tempIndex = 0;
        while (indexMoinsValeureux == -1) {
            Fraction valeur = this.table[tempIndex][indexMaximisant];
            if (valeur.superieurStrictZero()) {
                indexMoinsValeureux = tempIndex;
            }
            tempIndex++;
        }
        if (indexMoinsValeureux >= this.base.getTaille())
            throw new Exception(
                    "Aucune variable de la base ne peut sortir de la base, impossible de continuer l'algorithme");
        // System.out.println("ici = " + indexMoinsValeureux);
        for (int i = tempIndex; i < base.getTaille(); i++) {
            if (this.table[i][indexMaximisant].inferieurOuEgalZero())
                continue;
            Fraction valeurSolutionProbable = getValeurVariableBasePourEtreRemplace(this.table[indexMoinsValeureux],
                    indexMaximisant);
            Fraction valeurATester = getValeurVariableBasePourEtreRemplace(this.table[i], indexMaximisant);
            if (Fraction.superieur(valeurSolutionProbable, valeurATester))
                indexMoinsValeureux = i;
        }

        return this.base.getListeVariable()[indexMoinsValeureux];
    }

    public boolean auMoins1VariableBasePeutSortir(int colonneRemplacante) {
        for (int i = 0; i < this.table.length; i++) {
            if (table[i][colonneRemplacante].superieurStrictZero())
                return true;
        }
        return false;
    }

    // Pour une ligne de variable de base, obtenir la valeur du variable dans la
    // base
    public Fraction getValeurVariableBasePourEtreRemplace(Fraction[] ligne, int colonneRemplacante) {
        Fraction valeur = Fraction.diviser(ligne[ligne.length - 1], ligne[colonneRemplacante]);
        // Fraction valeur = ligne[ligne.length - 1] / ligne[colonneRemplacante];
        return valeur;
    }

    // Faire rentrer l'element le plus maximisant et faire sortir la variable de
    // base la moins valeureuse
    public Variable getVariableLePlusMaximisant() {
        Fraction[] ligneObjective = this.table[this.table.length - 1];
        int indexMaximum = 0;
        for (int i = 1; i < ligneObjective.length - 1; i++) {
            if (Fraction.inferieur(ligneObjective[indexMaximum], ligneObjective[i])) {
                indexMaximum = i;
            }
        }
        // return
        return this.systeme.getListeVariable().get(indexMaximum);
    }

    public Systeme getSysteme() {
        return systeme;
    }

    public void setSysteme(Systeme systeme) {
        this.systeme = systeme;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public Fraction[][] getTable() {
        return table;
    }

    public void setTable(Fraction[][] table) {
        this.table = table;
    }

    public int[] getIndicePivot() {
        return indicePivot;
    }

    public void setIndicePivot(int[] indicePivot) {
        this.indicePivot = indicePivot;
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

    
    
}
