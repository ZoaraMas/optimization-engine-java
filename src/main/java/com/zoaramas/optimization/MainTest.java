package com.zoaramas.optimization;


import java.util.ArrayList;

import com.zoaramas.optimization.logic.*;
import com.zoaramas.optimization.logic.FonctionPack.Contrainte;


public class MainTest {
    public static void main(String[] args) throws Exception {
        System.out.println("hello world");

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
        // Table

        // // Test de la fonction objectif
        // float[] ValeursVariables = new float[2];
        // ValeursVariables[0] = 1;
        // ValeursVariables[1] = 2;

        // HashMap<Variable, Float> mapVariables = new HashMap<>();
        // mapVariables.put(x, 1f);
        // mapVariables.put(y, 2f);
        // System.out.println("f(1, 2) = " + f.f(mapVariables));
    }
}