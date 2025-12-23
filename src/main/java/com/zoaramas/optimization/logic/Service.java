package com.zoaramas.optimization.logic;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.zoaramas.optimization.logic.Plne.PlneExt;
import com.zoaramas.optimization.logic.FonctionPack.Contrainte;

public class Service {

    public static float getSigneFromString(String s) {
        if (s.equals(">"))
            return 0;
        if (s.equals(">="))
            return 1;
        if (s.equals("<"))
            return 2;
        if (s.equals("<="))
            return 3;
        if (s.equals("="))
            return 4;
        else
            return -1;
    }

    public static Signe getSigneFromFloat(float f) {
        if (f == 0)
            return Signe.superieur;
        if (f == 1)
            return Signe.superieurOuEgal;
        if (f == 2)
            return Signe.inferieur;
        if (f == 3)
            return Signe.inferieurOuEgal;
        if (f == 4)
            return Signe.egal;
        return null;
    }

    // Dans une liste 2D de Fraction, retourner le nombre de colonne le plus
    // important
    public static int getNbColonnePlusGrand(Fraction[][] fraction) {
        int max = 0;
        for (int i = 0; i < fraction.length; i++) {
            Fraction[] liste = fraction[i];
            if (liste.length > max)
                max = liste.length;
        }
        return max;
    }

    // A utiliser pour l'affichage GUI
    // Ici le nombre de colonne doit etre le meme sur toutes les lignes
    public static Fraction[][] clone(Fraction[][] fraction) {
        int nbColonne = getNbColonnePlusGrand(fraction);
        Fraction[][] result = new Fraction[fraction.length][];
        for (int i = 0; i < fraction.length; i++) {
            Fraction[] liste = fraction[i];
            Fraction[] listeClone = new Fraction[nbColonne];
            for (int j = 0; j < liste.length; j++) {
                listeClone[j] = liste[j];
                listeClone[j].simplifier();
            }
            // Mettre a null les colonnes restantes
            for (int j = liste.length; j < nbColonne; i++) {
                listeClone[j] = null;
            }
            result[i] = listeClone;
        }
        return result;
    }

    public static Result[] getResultFromAff(ArrayList<ArrayList<JTextField>> fields, ArrayList<JComboBox> comboBoxes,
            boolean maximiser)
            throws Exception {
        ArrayList<ArrayList<Float>> array = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            ArrayList<Float> underList = new ArrayList<>();
            ArrayList<JTextField> underFields = fields.get(i);
            for (int j = 0; j < underFields.size(); j++) {
                underList.add(Float.parseFloat(underFields.get(j).getText()));
                if (i != 0 && j == underFields.size() - 2) { // On prend le signe depuis le comboBox
                    underList.add(getSigneFromString((String) (comboBoxes.get(i - 1)).getSelectedItem()));
                }
            }
            array.add(underList);
        }
        return getResult(array, maximiser);
    }

    // Fonctionalite principale
    public static Result[] getResult(ArrayList<ArrayList<Float>> fields, boolean maximiser) throws Exception {
        System.out.println("mickey");
        int nombreVariable = fields.get(0).size();
        int nombreContraintes = fields.size() - 1;
        // Variables systeme
        ArrayList<Variable> listeVariableSysteme = new ArrayList<>();
        for (int i = 0; i < nombreVariable; i++) {
            listeVariableSysteme.add(new Variable("x" + (i + 1), TypeVariable.direct));
        }

        // Fonction objective
        ArrayList<Float> listeObjective = fields.get(0);
        ArrayList<Terme> listeTermeObjectif = new ArrayList<>();
        for (int i = 0; i < nombreVariable; i++) {
            listeTermeObjectif.add(new Terme(listeVariableSysteme.get(i), listeObjective.get(i)));
        }
        Fonction objectif = new Fonction(listeTermeObjectif);

        // Liste des contraintes
        ArrayList<Contrainte> listeContrainte = new ArrayList<>();
        for (int k = 0; k < nombreContraintes; k++) {
            ArrayList<Terme> listeTermeContrainte1 = new ArrayList<>();
            ArrayList<Float> liste = fields.get(1 + k); // liste actuelle dans l'iterraiton
            for (int i = 0; i < nombreVariable; i++) {
                listeTermeContrainte1.add(new Terme(listeVariableSysteme.get(i), liste.get(i)));
            }
            float secondMembre1 = liste.get(liste.size() - 1);
            Signe signe = getSigneFromFloat(liste.get(liste.size() - 2));
            Contrainte contrainte1 = new Contrainte(listeTermeContrainte1, signe, secondMembre1);
            listeContrainte.add(contrainte1);
        }

        // Les contraintes de signes
        ArrayList<Contrainte> listeContrainteSigne = new ArrayList<>();
        for (int i = 0; i < nombreVariable; i++) {
            ArrayList<Terme> listeTermeContrainteSigne1 = new ArrayList<>();
            listeTermeContrainteSigne1.add(new Terme(listeVariableSysteme.get(i), 1));
            float secondMembreSigne1 = 0;
            Contrainte contrainteSigne1 = new Contrainte(listeTermeContrainteSigne1, Signe.superieurOuEgal,
                    secondMembreSigne1);
            listeContrainteSigne.add(contrainteSigne1);
        }

        // On obtiens le systeme a traiter
        Systeme systeme = new Systeme(listeVariableSysteme, objectif, listeContrainte, listeContrainteSigne);

        System.out.println("DEBUT LOG ENTIER");
        // PLNE, JE MET ICI POUR GARDER LES PRINTS DE LA FONCTION ORIGINAL
        Result[] resultEntier = PlneExt.plner(systeme, null, null, true);
        System.out.println("FIN LOG ENTIER");

        systeme.str();
        Result result = systeme.simplexerMaxMin(maximiser);
        System.out.println("==================");
        System.out.println("RESULTAT FINAL ENTIER");
        result.printResult();
        Result[] finalResult = new Result[2];
        finalResult[0] = result;

        finalResult[1] = resultEntier[0];
        return finalResult;
    }

    public static Result getSystemeTemp() throws Exception {
        Variable x = new Variable("x", TypeVariable.direct);
        Variable y = new Variable("y", TypeVariable.direct);
        Variable z = new Variable("z", TypeVariable.direct);

        ArrayList<Variable> listeVariableSysteme = new ArrayList<>();
        listeVariableSysteme.add(x);
        listeVariableSysteme.add(y);
        listeVariableSysteme.add(z);

        ArrayList<Terme> listeTermeObjectif = new ArrayList<>();
        listeTermeObjectif.add(new Terme(x, 40));
        listeTermeObjectif.add(new Terme(y, 30));
        listeTermeObjectif.add(new Terme(z, 50));
        Fonction objectif = new Fonction(listeTermeObjectif);

        ArrayList<Terme> listeTermeContrainte1 = new ArrayList<>();
        listeTermeContrainte1.add(new Terme(x, 3));
        listeTermeContrainte1.add(new Terme(y, 2));
        listeTermeContrainte1.add(new Terme(z, 4));
        float secondMembre1 = 120;
        Contrainte contrainte1 = new Contrainte(listeTermeContrainte1, Signe.inferieurOuEgal, secondMembre1);

        ArrayList<Terme> listeTermeContrainte2 = new ArrayList<>();
        listeTermeContrainte2.add(new Terme(x, 2));
        listeTermeContrainte2.add(new Terme(y, 1));
        listeTermeContrainte2.add(new Terme(z, 3));
        float secondMembre2 = 100;
        Contrainte contrainte2 = new Contrainte(listeTermeContrainte2, Signe.inferieurOuEgal, secondMembre2);

        ArrayList<Terme> listeTermeContrainte3 = new ArrayList<>();
        listeTermeContrainte3.add(new Terme(x, 1));
        listeTermeContrainte3.add(new Terme(y, 3));
        listeTermeContrainte3.add(new Terme(z, 2));
        float secondMembre3 = 90;
        Contrainte contrainte3 = new Contrainte(listeTermeContrainte3, Signe.inferieurOuEgal, secondMembre3);

        ArrayList<Contrainte> listeContrainte = new ArrayList<>();
        listeContrainte.add(contrainte1);
        listeContrainte.add(contrainte2);
        listeContrainte.add(contrainte3);

        ArrayList<Terme> listeTermeContrainteSigne1 = new ArrayList<>();
        listeTermeContrainteSigne1.add(new Terme(x, 1));
        float secondMembreSigne1 = 0;
        Contrainte contrainteSigne1 = new Contrainte(listeTermeContrainteSigne1, Signe.superieurOuEgal,
                secondMembreSigne1);

        ArrayList<Terme> listeTermeContrainteSigne2 = new ArrayList<>();
        listeTermeContrainteSigne2.add(new Terme(y, 1));
        float secondMembreSigne2 = 0;
        Contrainte contrainteSigne2 = new Contrainte(listeTermeContrainteSigne2, Signe.superieurOuEgal,
                secondMembreSigne2);

        ArrayList<Terme> listeTermeContrainteSigne3 = new ArrayList<>();
        listeTermeContrainteSigne3.add(new Terme(z, 1));
        float secondMembreSigne3 = 0;
        Contrainte contrainteSigne3 = new Contrainte(listeTermeContrainteSigne3, Signe.superieurOuEgal,
                secondMembreSigne3);

        ArrayList<Contrainte> listeContrainteSigne = new ArrayList<>();
        listeContrainteSigne.add(contrainteSigne1);
        listeContrainteSigne.add(contrainteSigne2);
        listeContrainteSigne.add(contrainteSigne3);
        Systeme systeme = new Systeme(listeVariableSysteme, objectif, listeContrainte, listeContrainteSigne);

        systeme.str();
        Result result = systeme.simplexerMaxMin(true);
        System.out.println("==================");
        System.out.println("RESULTAT FINAL");
        result.printResult();
        return result;
    }
}
