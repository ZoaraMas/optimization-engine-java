package com.zoaramas.optimization.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zoaramas.optimization.logic.FonctionPack.Contrainte;

public class Fonction {
    protected ArrayList<Terme> listeTerme;

    public Fonction(Terme terme) {
        ArrayList<Terme> temp = new ArrayList<>();
        temp.add(terme);
        this.listeTerme = temp;
    }

    public Fonction(ArrayList<Terme> listeTerme) {
        this.listeTerme = listeTerme;
    }

    // negativer toutes les coefficients de la fonction
    public void negativerFonction() {
        for(int i = 0; i < this.listeTerme.size();i ++) {
            Terme terme = this.listeTerme.get(i);
            terme.setCoefficient(terme.getCoefficient() * -1);
        }
    }


    // Obtenir la derniere ligne apres avoir effectue la premiere phase
    public Fraction[] getDerniereLigneFonctionTableSimplexFraction(Variable[] listeVariable,
            HashMap<Variable, Fraction> resultatPhase1Simplex) throws Exception {
        Fraction[] result = new Fraction[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariableFraction(listeVariable[i]);
        }
        result[listeVariable.length] = this.fFraction(resultatPhase1Simplex).mult(-1);
        return result;
    }

    public float[] getDerniereLigneFonctionTableSimplex(Variable[] listeVariable,
            HashMap<Variable, Float> resultatPhase1Simplex) throws Exception {
        float[] result = new float[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariable(listeVariable[i]);
        }
        result[listeVariable.length] = (-1) * this.f(resultatPhase1Simplex);
        return result;
    }

    public int getNombreVariableNonDirect() {
        int count = 0;
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            if (terme.getVariable().getType() != TypeVariable.direct) {
                count++;
            }
        }
        return count;
    }

    // DANS LE CONTEXTE DE TEST SIMPLEX 2 PHASES, OBTENIR LE SIGNE DE LA FONCTION SI
    // ON NE DONNE PAS TOUTES LES VALEURS DES VARIABLES DE TERMES
    // RETOURNE 0 SI NUL, 1 SI POSITIF ET -1 SI NEGATIF
    public int getSigneFonctionPourTestSimplex2Phases() throws Exception {
        // for (int i = 0; i < this.listeTerme.size(); i++) {
        // Terme terme = listeTerme.get(i);
        // System.out.println(terme.getVariable().getNom());
        // }
        int result = 0;
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            if (terme.getVariable().getType() != TypeVariable.direct) {
                if (terme.getCoefficient() > 0)
                    result = 1;
                else if (terme.getCoefficient() < 0)
                    result = -1;
                return result;
            }
        }
        throw new Exception("ERREUR DANS LA METHODE ");
    }

    // obtenir une lise de 0 selon le nombre de terme
    public HashMap<Variable, Float> getMapValeurAZero() {
        HashMap<Variable, Float> result = new HashMap<>();
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            result.put(terme.getVariable(), 0f);
        }
        return result;
    }

    public Fonction clone() {
        return new Fonction(Terme.cloneListeTerme(listeTerme));
    }

    // Obtenir la derniere ligne de la table simplex
    // Uniquement pour le simplex a une phase, toutes les variables directs seront
    // mis a 0
    public float[] getDerniereLigneFonctionTableSimplex(Variable[] listeVariable) throws Exception {
        float[] result = new float[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariable(listeVariable[i]);
        }
        float[] listeVariableAZero = getListeValeurAZero();
        result[listeVariable.length] = (-1) * this.f(listeVariableAZero);
        return result;
    }

    public Fraction[] getDerniereLigneFonctionTableSimplexFraction(Variable[] listeVariable) throws Exception {
        Fraction[] result = new Fraction[listeVariable.length + 1];
        for (int i = 0; i < listeVariable.length; i++) {
            result[i] = this.getCoeffVariableFraction(listeVariable[i]);
        }
        float[] listeVariableAZero = getListeValeurAZero();
        result[listeVariable.length] = new Fraction((-1) * this.f(listeVariableAZero));
        return result;
    }

    // Obtenir la valeur de la fonction avec les elements de la base
    // Utilise en simplex 1 SEULE PARTIE
    public float fValeurBase(Base b) throws Exception {
        // Change juste la valeur des variables directs en 0
        float[] coeffZero = new float[listeTerme.size()];
        for (int i = 0; i < coeffZero.length; i++) {
            coeffZero[i] = 0;
        }
        return this.f(coeffZero);
    }

    // Obtenir le coeeficient d'une variable
    public float getCoeffVariable(Variable v) {
        for (int i = 0; i < listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            if (terme.getVariable().equals(v))
                return terme.getCoefficient();
        }
        return 0;
    }

    public Fraction getCoeffVariableFraction(Variable v) throws Exception {
        for (int i = 0; i < listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            if (terme.getVariable().equals(v))
                return new Fraction(terme.getCoefficient());
        }
        return new Fraction();
    }

    // Display
    public String str() {
        String result = new String();
        for (int i = 0; i < listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            result += (terme.str() + " ");
        }
        return result;
    }

    // obtenir une lise de 0 selon le nombre de terme
    public float[] getListeValeurAZero() {
        float[] result = new float[this.listeTerme.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = 0;
        }
        return result;
    }

    // Verifie si toutes les variables des termes de la fonction appartiennent a une
    // liste de variable
    public boolean checkTermVariables(List<Variable> allowedVariables) {
        for (Terme terme : this.listeTerme) {
            Variable variableDuTerme = terme.getVariable();
            if (!Variable.contains(allowedVariables, variableDuTerme))
                return false; // Si une variable du terme n'est pas dans la liste autorisée, retourner false
        }
        return true; // Si toutes les variables des termes sont dans la liste autorisée, retourner
                     // true
    }

    public Fraction fFraction(HashMap<Variable, Fraction> mapVariables) throws Exception {
        Fraction result = new Fraction();
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            Fraction coeff = mapVariables.get(terme.getVariable());
            if (coeff != null) {
                Fraction mult = coeff.mult(terme.getCoefficient());
                result = Fraction.additioner(result, mult);
                result.simplifier(); 
            } else {
                // On n'ajoute rien, c'est comme ajouter 0
            }
        }
        return result;
    }

    // Retourne la valeur de la fonction avec des valeurs de variable dans un
    // hashmap, les variables non mentionne dans le hashmap seront mis en 0
    public float f(HashMap<Variable, Float> mapVariables) {
        float result = 0;
        for (int i = 0; i < this.listeTerme.size(); i++) {
            Terme terme = listeTerme.get(i);
            Float coeff = mapVariables.get(terme.getVariable());
            if (coeff != null) {
                result += coeff * terme.getCoefficient();
            } else {
                // On n'ajoute rien, c'est comme ajouter 0
            }
        }
        return result;
    }

    // Retourne la valeur de la fonction avec les valeurs de bases
    // public float fFraction(float[] ValeursVariables) throws Exception {
    //     if (ValeursVariables.length != listeTerme.size())
    //         throw new Exception(
    //                 "Le nombre de valeurs de variable ne correspond pas au nombre de variable de la fonction");
    //     Fraction result = new Fraction();
    //     for (int i = 0; i < ValeursVariables.length; i++) {
    //         Terme terme = listeTerme.get(i);
    //         Fraction mult = Fraction.multiplier(terme.getCoefficient(), ValeursVariables[i]);
    //         result += terme.getCoefficient() * ValeursVariables[i];
    //     }
    //     return result;
    // }

    public float f(float[] ValeursVariables) throws Exception {
        if (ValeursVariables.length != listeTerme.size())
            throw new Exception(
                    "Le nombre de valeurs de variable ne correspond pas au nombre de variable de la fonction");
        float result = 0;
        for (int i = 0; i < ValeursVariables.length; i++) {
            Terme terme = listeTerme.get(i);
            result += terme.getCoefficient() * ValeursVariables[i];
        }
        return result;
    }
}
