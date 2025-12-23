package com.zoaramas.optimization.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Result {
    private Fraction result;
    private HashMap<Variable, Fraction> liste;
    private boolean maximiser;

    // historique des tables
    private ArrayList<Fraction[][]> historique;
    private ArrayList<String> historiqueBase;
    private ArrayList<ArrayList<Variable>> historiqueVariable;
    private int indexChangementPhase;

    public Result(Fraction result, HashMap<Variable, Fraction> liste, boolean maximiser) {
        this.result = result;
        this.liste = liste;
        this.maximiser = maximiser;
    }

    public boolean isStrictementPlusGrandQue(Result r) {
        if (Fraction.superieur(this.result, r.getResult()))
            return true;
        return false;
    }

    // Obtenir la premiere variable fractionnaire avec sa valeur
    // Retourne une seule valeur, entry pour une seule valeurse
    public Map.Entry<Variable, Fraction> getFirstFractionalVariableValue() {
        for (Map.Entry<Variable, Fraction> entry : liste.entrySet()) {
            Fraction temp = entry.getValue();
            if (!temp.isEntier() && entry.getKey().getType().equals(TypeVariable.direct)) {
                System.out.println("On choisit la variable = ");
                System.out.println(temp);
                System.out.println(entry.getKey());
                Map.Entry<Variable, Fraction> result = new Map.Entry<Variable, Fraction>() {
                    @Override
                    public Variable getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public Fraction getValue() {
                        return temp;
                    }

                    @Override
                    public Fraction setValue(Fraction value) {
                        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
                    }
                };
                return result;
            }
            ;
        }
        return null;
    }

    // Verifie si le resultat est entier
    public boolean isEntier() {
        for (Map.Entry<Variable, Fraction> entry : liste.entrySet()) {
            Fraction temp = entry.getValue();
            if (!temp.isEntier())
                return false;
        }
        if (!result.isEntier())
            return false;
        return true;
    }

    public void setHistoriqueVariable(ArrayList<ArrayList<Variable>> histo) {
        this.historiqueVariable = histo;
    }

    public void setHistorique(ArrayList<Fraction[][]> histo) {
        this.historique = histo;
    }

    public String str() throws Exception {
        return getResultString();
    }

    public void printResult() throws Exception {
        System.out.println(getResultString());
    }

    public String getResultString() throws Exception {
        String temp = "max = ";
        if (!this.maximiser)
            temp = "min = ";
        String finalResult = (temp + result.getValue());
        finalResult += "\n" + getResultatSimplex(liste);
        return finalResult;
    }

    public static void printResultatSimplex(HashMap<Variable, Fraction> map) throws Exception {
        System.out.println(getResultatSimplex(map));
    }

    public static String getResultatSimplex(HashMap<Variable, Fraction> map) throws Exception {
        String result = "(";
        int i = 0;
        for (Map.Entry<Variable, Fraction> entry : map.entrySet()) {
            result += entry.getKey().getNom() + " = " + entry.getValue().getValue();
            if (i != map.size() - 1) {
                result += ", ";
            }
            i++;
        }
        return result + ")";
    }

    public Fraction getResult() {
        return result;
    }

    public void setResult(Fraction result) {
        this.result = result;
    }

    public HashMap<Variable, Fraction> getListe() {
        return liste;
    }

    public void setListe(HashMap<Variable, Fraction> liste) {
        this.liste = liste;
    }

    public boolean isMaximiser() {
        return maximiser;
    }

    public void setMaximiser(boolean maximiser) {
        this.maximiser = maximiser;
    }

    public ArrayList<Fraction[][]> getHistorique() {
        return historique;
    }

    public ArrayList<ArrayList<Variable>> getHistoriqueVariable() {
        return historiqueVariable;
    }

    public int getIndexChangementPhase() {
        return indexChangementPhase;
    }

    public void setIndexChangementPhase(int indexChangementPhase) {
        this.indexChangementPhase = indexChangementPhase;
    }

    public ArrayList<String> getHistoriqueBase() {
        return historiqueBase;
    }

    public void setHistoriqueBase(ArrayList<String> historiqueBase) {
        this.historiqueBase = historiqueBase;
    }

}
