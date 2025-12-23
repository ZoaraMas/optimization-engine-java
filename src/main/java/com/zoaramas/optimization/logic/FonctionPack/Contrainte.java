package com.zoaramas.optimization.logic.FonctionPack;

import java.util.ArrayList;
import java.util.HashMap;

import com.zoaramas.optimization.logic.*;


public class Contrainte extends Fonction {
    private Signe signe;
    private float secondMembre;

    public Contrainte(Terme terme, Signe signe, float secondMembre) {
        super(terme);
        this.signe = signe;
        this.secondMembre = secondMembre;
    }

    public Contrainte(ArrayList<Terme> listeTerme, Signe signe, float secondMembre) {
        super(listeTerme);
        this.signe = signe;
        this.secondMembre = secondMembre;
    }

    public Contrainte(ArrayList<Terme> listeTerme) {
        super(listeTerme);
    }

    public Variable getPrioriteArtificielleApresEcart() throws Exception {
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Variable v = this.listeTerme.get(i).getVariable();
            if (v.getType().equals(TypeVariable.artificielle)
                    || (v.getType().equals(TypeVariable.ecart) && !this.isArtificielle()))
                return v;
        }
        throw new Exception("Il n'y a que des variables dans la contrainte, erreur");
    }

    // Verifier si la contrainte possede une variable artificielle
    public boolean isArtificielle() {
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = this.listeTerme.get(i);
            if (terme.getVariable().getType().equals(TypeVariable.artificielle))
                return true;
        }
        return false;
    }

    // Cloner une contrainte jusqu'au bout
    // Utilisation: Nouvelles contraintes de signes
    // public ArrayList<Contrainte> clonerContrainteSigne() {

    // }

    public void addVariableArtificielle(Variable v) {
        Terme terme = new Terme(v, 1f);
        this.listeTerme.add(terme);
    }

    // Obtenir un tableau de contrainte par rapport a des variables
    public float[] getAllCoeffVariable(Variable[] listeVariable) {
        float[] result = new float[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariable(listeVariable[i]);
        }
        result[listeVariable.length] = this.secondMembre;
        return result;
    }

    public Fraction[] getAllCoeffVariableFraction(Variable[] listeVariable) throws Exception {
        Fraction[] result = new Fraction[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariableFraction(listeVariable[i]);
        }
        result[listeVariable.length] = new Fraction(this.secondMembre);
        return result;
    }

    // Cloner une liste de contrainte
    public static ArrayList<Contrainte> clonerListeContrainte(ArrayList<Contrainte> liste) {
        ArrayList<Contrainte> result = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            result.add(liste.get(i).clonerContrainte());
        }
        return result;
    }

    // Cloner une contrainte completement
    public Contrainte clonerContrainte() {
        ArrayList<Terme> nouvelleListeTerme = Terme.cloneListeTerme(listeTerme);
        return new Contrainte(nouvelleListeTerme, signe, secondMembre);
    }

    // Clone dur depuis la classe mere
    public Contrainte clone() {
        return new Contrainte(Terme.cloneListeTerme(listeTerme));
    }

    // Ajouter a une variable d'ecart a la fonction
    // si le signe est <: le signe de la variable d'ecart doit etre + et vice versa
    public void ajouterVariableEcart(Signe signe, Variable variableEcart) {
        float coefficient = -1f;
        if (signe.equals(Signe.inferieur) || signe.equals(Signe.inferieurOuEgal))
            coefficient = 1;
        Terme terme = new Terme(variableEcart, coefficient);
        this.listeTerme.add(terme);
    }

    // modifier une contrainte
    // Dans le contexte de la resolution simplex a 1 seule etape
    public Contrainte modifierContrainte(Signe signe) {
        Contrainte result = (Contrainte) this.clone();
        result.setSigne(signe);
        result.setSecondMembre(this.secondMembre);
        return result;
    }

    public String str() {
        String result = super.str();
        result += (" " + signe.str() + " " + secondMembre);
        return result;
    }

    // TESTER UNIQUEMENT AVEC DES EGALITES
    // Verifier l'etat booleenne d'une contrainte
    public boolean tester(float unique) throws Exception {
        if (this.listeTerme.size() != 1)
            throw new Exception("La contrainte ne doit pas contenir plus d'une variable ");
        float[] tab = new float[1];
        tab[0] = unique;
        float temp = this.f(tab);
        return Signe.tester(signe, temp, secondMembre);
    }

    public boolean tester(HashMap<Variable, Float> ValeursVariables) throws Exception {
        if (!this.signe.equals(Signe.egal))
            throw new Exception("La contrainte n'est pas une egalite, impossible de tester avec un HashMap");
        // si le nombre de variable direct de la fonction est la meme que la taille de
        // la liste
        // de valeursVariables, alors on test direct l'egaliter
        // for (Variable variable : ValeursVariables.keySet()) {
        // System.out.println("Variable: " + variable.getNom() + ", Value: " +
        // ValeursVariables.get(variable));
        // }
        // System.out.println(this.getNombreVariableNonDirect());
        if (this.getNombreVariableNonDirect() == 0) {
            float f = this.f(ValeursVariables);
            return Signe.tester(this.signe, f, this.secondMembre);
        }
        int signe = this.getSigneFonctionPourTestSimplex2Phases();
        // si jamais c'est une egalite sans variable d'ecart donc
        int signeSecondMembre = 0;
        if (this.secondMembre > 0)
            signeSecondMembre = 1;
        else if (this.secondMembre < 0)
            signeSecondMembre = -1;
        if (signe != signeSecondMembre)
            return false; // si le signe de la fonction n'est pas le meme que le signe du second membre,
                          // on ne peut pas tester l'egalite
        return true;
    }

    public Signe getSigne() {
        return signe;
    }

    public void setSigne(Signe signe) {
        this.signe = signe;
    }

    public float getSecondMembre() {
        return secondMembre;
    }

    public void setSecondMembre(float secondMembre) {
        this.secondMembre = secondMembre;
    }
}
