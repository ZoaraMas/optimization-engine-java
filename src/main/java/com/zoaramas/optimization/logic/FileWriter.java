package com.zoaramas.optimization.logic;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public class FileWriter {
    public static void viderFichier(String fileName) {  
        writeToFile(fileName, "");
    }

    public static void writeForm2(ArrayList<ArrayList<JTextField>> fields, ArrayList<JComboBox> comboBoxes) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            for (int j = 0; j < fields.get(i).size(); j++) {
                content.append(fields.get(i).get(j).getText()).append(" ");
                if (i != 0 && j == fields.get(i).size() - 2) {
                    content.append(comboBoxes.get(i - 1).getSelectedItem()).append(" ");
                }
            }
            // content.append("\n");
        }
        writeToFile("form2.txt", content.toString());
    }

    public static void writeForm1(JTextField nombreVariables, JTextField nombreContraintes) {
        String fileName = "form1.txt";
        String[] previousContent = FileReader.readForm1File("form1.txt");
        String content = nombreVariables.getText() + "\n" + nombreContraintes.getText();
        if(previousContent[0].equals(nombreVariables.getText()) && previousContent[1].equals(nombreContraintes.getText())) {}
        // else viderFichier("form2.txt");
        // On ne va plus vider le fichier
        FileWriter.writeToFile(fileName, content);
    }

    public static void writeToFile(String fileName, String content) {
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName)) {
            writer.write(content);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
