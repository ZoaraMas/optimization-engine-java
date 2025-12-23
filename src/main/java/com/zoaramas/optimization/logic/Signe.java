package com.zoaramas.optimization.logic;

public enum Signe {
    egal("="),
    inferieur("<"),
    superieur(">"),
    inferieurOuEgal("<="),
    superieurOuEgal(">=");

    private final String signe;

    Signe(String signe) {
        this.signe = signe;
    }

    // Verifier un signe
    public static boolean tester(Signe signe, float valeur, float secondMembre){
        if(signe.equals(Signe.egal) && valeur == secondMembre) return true;
        else if(signe.equals(Signe.inferieur) && valeur < secondMembre) return true;
        else if(signe.equals(Signe.superieur) && valeur > secondMembre) return true;
        else if(signe.equals(Signe.inferieurOuEgal) && valeur <= secondMembre) return true;
        else if(signe.equals(Signe.superieurOuEgal) && valeur >= secondMembre) return true;
        return false;
    }

    public String getSigne() {
        return this.name();
    }

    public String str() {
        return this.signe;
    }
}
