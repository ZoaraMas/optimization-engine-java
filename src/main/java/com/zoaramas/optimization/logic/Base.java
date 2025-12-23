package com.zoaramas.optimization.logic;

import java.util.ArrayList;

public class Base {
    private int taille;
    private Variable[] listeVariable; // Liste de variable present dans la base
    private ArrayList<Variable> listeVariableTotale;

    public Base(int taille, Variable[] listeVariable, ArrayList<Variable> listeVariableTotale) throws Exception {
        if (taille != listeVariable.length)
            throw new Exception("La taille de la base ne correspond pas au nombre de variable dans la liste " + taille
                    + " != " + listeVariable.length);
        this.taille = taille;
        this.listeVariable = listeVariable;
        this.listeVariableTotale = listeVariableTotale;
    }

    public boolean contientArtificielle() {
        for(int i = 0; i < listeVariable.length; i ++) {
            if(listeVariable[i].getType().equals(TypeVariable.artificielle)) return true;
        }
        return false;
    }
    // Verifie si une variable est dans la base et retourne l'indice dans la base
    public int getIndexInBase(Variable variable) {
        for (int i = 0; i < taille; i++) {
            if (this.listeVariable[i].equals(variable)) {
                return i;
            }
        }
        return -1;
    }
    
    public void permuter(Variable aRemplacer, Variable variableRentrante) throws Exception {
        int slotConcerne = Variable.getIndexInArray(this.listeVariable, aRemplacer);
        this.listeVariable[slotConcerne] = variableRentrante;
    }

    public String disp() {
        String result = "[";
        for (int i = 0; i < taille; i++) {
            result += "" + listeVariable[i].getNom();
        }
        return result + "]";
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public Variable[] getListeVariable() {
        return listeVariable;
    }

    public void setListeVariable(Variable[] listeVariable) {
        this.listeVariable = listeVariable;
    }

    public ArrayList<Variable> getListeVariableTotale() {
        return listeVariableTotale;
    }

    public void setListeVariableTotale(ArrayList<Variable> listeVariableTotale) {
        this.listeVariableTotale = listeVariableTotale;
    }

}