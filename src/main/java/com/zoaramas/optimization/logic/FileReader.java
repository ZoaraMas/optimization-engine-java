package com.zoaramas.optimization.logic;

public class FileReader {
    public static String[] readForm2File(String fileName) {
        java.io.File file = new java.io.File(fileName);
        if (file.length() == 0) {
            return null;
        }
        StringBuilder content = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim().split(" ");
    }

    public static String[] readForm1File(String fileName) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return lines.toArray(new String[0]);
    }

}
