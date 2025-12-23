package com.zoaramas.optimization.logic;

import java.util.ArrayList;

public class Terme {
    // Une terme est l'ensemble l'une variable et son coefficient
    private Variable variable;
    private float coefficient;
    
    public String str() {
        String result = "";
        if(coefficient > 0) result += "+";
        return result + coefficient + variable.getNom();
    }
    
    public Terme(Variable variable, float coefficient) {
        this.variable = variable;
        this.coefficient = coefficient;
    }

    public static ArrayList<Terme> cloneListeTerme(ArrayList<Terme> liste) {
        ArrayList<Terme> result = new ArrayList<>();
        for(int i = 0; i < liste.size(); i ++) {
            result.add(liste.get(i).clone());
        }
        return result;
    }

    public Terme clone() {
        Variable clonedVariable = variable.clone();
        return new Terme(clonedVariable, coefficient);
    }

    public Variable getVariable() {
        return variable;
    }
    public void setVariable(Variable variable) {
        this.variable = variable;
    }
    public float getCoefficient() {
        return coefficient;
    }
    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }

    
}
