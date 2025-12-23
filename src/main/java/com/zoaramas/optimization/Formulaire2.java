package com.zoaramas.optimization;


import javax.swing.*;

import com.zoaramas.optimization.logic.FileReader;
import com.zoaramas.optimization.logic.FileWriter;
import com.zoaramas.optimization.logic.Service;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Formulaire2 extends JPanel {
    private ArrayList<ArrayList<JTextField>> fields;
    private ArrayList<JComboBox> comboBoxes;
    private JButton okButton;
    private CardLayout layout;
    private Main win;

    // display constants
    private int currLine;
    private int currCol;
    private final int heightSpacing = 10;
    private final int smallSpace = 5;
    private final int mediumSpace = 10;
    private final int margin = 30;
    private final int eltWidth = 70;
    private final int eltHeight = 30;

    private boolean maximiser;

    public Formulaire2(Main window, CardLayout cardLayout) {
        this.win = window;
        this.layout = cardLayout;
        setLayout(null);

        this.fields = new ArrayList<>();
        this.comboBoxes = new ArrayList<>();
        this.okButton = new JButton("VALIDER");

        this.maximiser = false;
        // this.okButton.setPreferredSize(new Dimension(1, 1));
    }

    public void setup(int nombreVariable, int nombreContrainte, String maximiserString) {
        if(maximiserString.equals("max")) this.maximiser = true;
        getListForm1Contrainte(nombreVariable, nombreContrainte);
        okButton.setBounds(getCurrX(), getCurrY(), eltWidth, eltHeight);
        add(okButton);
        this.okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ecrire
                FileWriter.writeForm2(fields, comboBoxes);
                layout.next(win.getMainPanel());
                try {
                    // Utiliser la premiere ligne si on ne veut pas de la resultat entier par algorithme PLNE
                    // win.getPageResultat().createTables(Service.getResultFromAff(fields, comboBoxes, maximiser)[0]);
                    win.getPageResultat().createTables(Service.getResultFromAff(fields, comboBoxes, maximiser)[0], Service.getResultFromAff(fields, comboBoxes, maximiser)[1]);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(Formulaire2.this,
                            "Erreur lors du calcul des résultats : " + e1.getMessage(), "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void getListForm1Contrainte(int nombreVariable, int nombreContrainte) {
        String[] temptemp = FileReader.readForm2File("form2.txt");
        int temptempIndex = 0;
        this.currLine = 0;
        this.currCol = 0;
        // Contraintes uniquemnet d'abord
        for (int i = 0; i < nombreContrainte + 1; i++) {
            this.fields.add(new ArrayList<>());
            // ArrayList<JTextField> liste = new ArrayList<>();
            for (int j = 0; j < nombreVariable + 1; j++) {
                if (i == 0 && j == nombreVariable) {
                } else {
                    JTextField textField = new JTextField(5);
                    if (temptempIndex < temptemp.length)
                        textField.setText(temptemp[temptempIndex]);
                    temptempIndex++;
                    int x = getCurrX();
                    int y = getCurrY();
                    textField.setBounds(x, y, eltWidth, eltHeight);
                    add(textField);
                    this.fields.get(i).add(textField);
                    nextCol();
                    // On ajoute la variable a cote si ce n'est pas le second membre
                    if (j != nombreVariable) {
                        JLabel tempLabel = new JLabel("x" + (j + 1));
                        x = getCurrX();
                        y = getCurrY();
                        tempLabel.setBounds(x, y, eltWidth, eltHeight);
                        add(tempLabel);
                        nextCol();
                    }
                    // On rajoute le combo
                    if (j == nombreVariable - 1 && i != 0) {
                        String[] options = { ">", ">=", "<", "<=", "=" };
                        JComboBox comboSigne = new JComboBox<>(options);
                        if (temptempIndex < temptemp.length)
                            comboSigne.setSelectedItem(temptemp[temptempIndex]);
                        temptempIndex++;
                        x = getCurrX();
                        y = getCurrY();
                        comboSigne.setBounds(x, y, eltWidth, eltHeight);
                        add(comboSigne);
                        this.comboBoxes.add(comboSigne);
                        nextCol();
                    }
                    // Soit on ajoute "+" soit on ajoute un combobox
                }
            }
            nextLine();
            // this.fields.add(liste);
        }
        nextLine();
        revalidate();
        repaint();
    }

    public void nextLine() {
        this.currLine++;
        this.currCol = 0;
    }

    public void nextCol() {
        this.currCol++;
    }

    public int getCurrX() {
        return this.margin + ((this.eltWidth + smallSpace) * this.currCol) + smallSpace;
    }

    public int getCurrY() {
        return margin + ((eltHeight + heightSpacing) * this.currLine) + heightSpacing;
    }

}
