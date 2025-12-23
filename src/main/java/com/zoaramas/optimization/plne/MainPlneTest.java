package com.zoaramas.optimization.plne;

import java.util.ArrayList;

import com.zoaramas.optimization.logic.*;
import com.zoaramas.optimization.logic.FonctionPack.Contrainte;
import com.zoaramas.optimization.logic.Plne.PlneExt;


public class MainPlneTest {
      public static void main(String[] args) throws Exception {
        System.out.println("hello world");

        Variable x = new Variable("x", TypeVariable.direct);
        Variable y = new Variable("y", TypeVariable.direct);

        ArrayList<Variable> listeVariableSysteme = new ArrayList<>();
        listeVariableSysteme.add(x);
        listeVariableSysteme.add(y);

        ArrayList<Terme> listeTermeObjectif = new ArrayList<>();
        listeTermeObjectif.add(new Terme(x, 5));
        listeTermeObjectif.add(new Terme(y, 4));
        Fonction objectif = new Fonction(listeTermeObjectif);
        
        ArrayList<Terme> listeTermeContrainte1 = new ArrayList<>();
        listeTermeContrainte1.add(new Terme(x, 1));
        listeTermeContrainte1.add(new Terme(y, 1));
        float secondMembre1 = 5;
        Contrainte contrainte1 = new Contrainte(listeTermeContrainte1, Signe.inferieurOuEgal, secondMembre1);

        ArrayList<Terme> listeTermeContrainte2 = new ArrayList<>();
        listeTermeContrainte2.add(new Terme(x, 10));
        listeTermeContrainte2.add(new Terme(y, 6));
        float secondMembre2 = 45;
        Contrainte contrainte2 = new Contrainte(listeTermeContrainte2, Signe.inferieurOuEgal, secondMembre2);

        ArrayList<Contrainte> listeContrainte = new ArrayList<>();
        listeContrainte.add(contrainte1);
        listeContrainte.add(contrainte2);

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

        ArrayList<Contrainte> listeContrainteSigne = new ArrayList<>();
        listeContrainteSigne.add(contrainteSigne1);
        listeContrainteSigne.add(contrainteSigne2);
        Systeme systeme = new Systeme(listeVariableSysteme, objectif, listeContrainte, listeContrainteSigne);

        systeme.str();
        Result result = systeme.simplexerMaxMin(true);
        System.out.println("==================");
        System.out.println("RESULTAT FINAL");
        result.printResult();
        // PLNE
        System.out.println("===================");
        System.out.println("===================");
        System.out.println("PLNE");
        System.out.println("===================");
        System.out.println("===================");
        Result[] resultEntier = PlneExt.plner(systeme, null, null, true);
        System.out.println("LE RESULTAT FINAL ENTIER EST:");
        resultEntier[0].printResult();
    }
}
