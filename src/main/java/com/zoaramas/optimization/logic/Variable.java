package com.zoaramas.optimization.logic;

import java.util.ArrayList;
import java.util.List;

public class Variable {
    private String nom;
    private TypeVariable type;

    public Variable(String nom, TypeVariable type) {
        this.nom = nom;
        this.type = type;
    }

    public void afficher() {
        System.out.println("Variable: " + nom + ", type: " + type);
    }

    @Override
    public String toString() {
        return nom;
    }

    public static int getIndexInArray(Variable[] list, Variable v) throws Exception {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(v))
                return i;
        }
        throw new Exception("la variable n'a pas ete trouve dans la liste: " + v.getNom());
    }

    // Dans une liste de variable, retourner l'indice d'une variable
    public static int getIndexInList(ArrayList<Variable> list, Variable v) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(v))
                return i;
        }
        throw new Exception("la variable n'a pas ete trouve dans la liste: " + v.getNom());
    }

    public static String dispListVariable(ArrayList<Variable> list) {
        String result = "[";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i).getNom();
            if (i != list.size() - 1)
                result += ", ";
        }
        return result + "]";
    }

    // Changer arrayList<Variable> en tableau de variable
    public static Variable[] convertListToArray(ArrayList<Variable> liste) {
        Variable[] result = new Variable[liste.size()];
        for (int i = 0; i < liste.size(); i++) {
            result[i] = liste.get(i).clone();
        }
        return result;
    }

    public static boolean contains(List<Variable> liste, Variable v) {
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).equals(v))
                return true;
        }
        return false;
    }

    // Pour les hashmaps
    @Override
    public boolean equals(Object v) {
        if (this == v)
            return true;
        if (!(v instanceof Variable))
            return false;
        Variable other = (Variable) v;
        return (this.nom.equals(other.getNom()) && this.type.equals(other.getType()));
    }

    @Override
    public int hashCode() {
        return nom.hashCode(); // utilise le hash du nom (String)
    }
    // Fin pour les hashmaps

    public boolean equals(Variable v) {
        return (this.nom.equals(v.getNom()) && this.type.equals(v.getType()));
    }

    // Dans une liste de variable, retourner les variables qui sont de type ecart
    public static ArrayList<Variable> getListeVariable(ArrayList<Variable> liste, TypeVariable type) {
        ArrayList<Variable> result = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            Variable v = liste.get(i);
            if (v.getType().equals(type))
                result.add(v.clone());
        }
        return result;
    }

    public static ArrayList<Variable> cloneListVariable(ArrayList<Variable> liste) {
        ArrayList<Variable> result = new ArrayList<>();
        for (int i = 0; i < liste.size(); i++) {
            result.add(liste.get(i).clone());
        }
        return result;
    }

    public Variable clone() {
        return new Variable(nom, type);
    }

    // Afficher une liste de variable
    public static void printListVariable(ArrayList<Variable> liste) {
        for (int i = 0; i < liste.size(); i++) {
            System.out.println(liste.get(i).getNom());
        }
    }

    public static void printListVariable(Variable[] liste) {
        for (int i = 0; i < liste.length; i++) {
            System.out.println(liste[i].getNom());
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeVariable getType() {
        return type;
    }

    public void setType(TypeVariable type) {
        this.type = type;
    }

}
