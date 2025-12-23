package com.zoaramas.optimization.logic;

import java.util.ArrayList;

import com.zoaramas.optimization.logic.FonctionPack.Contrainte;

public class Util {
    public static void appendArrayListOfArrays(ArrayList<Fraction[][]> destination, ArrayList<Fraction[][]> source) {
        if (destination == null || source == null) return;
        destination.addAll(source);
    }
    public static void appendArrayListOfArraysString(ArrayList<String> destination, ArrayList<String> source) {
        if (destination == null || source == null) return;
        destination.addAll(source);
    }
}
