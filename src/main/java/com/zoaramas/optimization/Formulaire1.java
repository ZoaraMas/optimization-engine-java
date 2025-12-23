package com.zoaramas.optimization;

import javax.swing.*;

import com.zoaramas.optimization.logic.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Formulaire1 extends JPanel {
    private JTextField nombreVariables;
    private JTextField nombreContraintes;
    private JComboBox maxMin;
    private JButton okButton;
    private CardLayout layout;
    private Main win;

    public Formulaire1(Main window, CardLayout cardLayout) {
        this.win = window;
        this.layout = cardLayout;
        setLayout(new FlowLayout());

        String[] temp = FileReader.readForm1File("form1.txt");
        JLabel nombreVariableLabel = new JLabel("nombre de variable");
        JLabel nombreContrainteLabel = new JLabel("nombre de contrainte");
        this.nombreVariables = new JTextField(20);
        this.nombreVariables.setText(temp[0]);
        this.nombreContraintes = new JTextField(20);
        this.nombreContraintes.setText(temp[1]);
        String[] comboContext = {"max", "min"};
        this.maxMin = new JComboBox<>(comboContext);
        this.okButton = new JButton("VALIDER");

        this.okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ecrire
                FileWriter.writeForm1(nombreVariables, nombreContraintes);
                win.setupForm2(Integer.parseInt(nombreVariables.getText()),
                        Integer.parseInt(nombreContraintes.getText()), (String)maxMin.getSelectedItem());
                layout.next(win.getMainPanel());
            }
        });

        add(nombreVariableLabel);
        add(nombreVariables);
        add(nombreContrainteLabel);
        add(nombreContraintes);
        add(maxMin);
        add(okButton);
    }

    public JTextField getNombreVariables() {
        return nombreVariables;
    }

    public void setNombreVariables(JTextField nombreVariables) {
        this.nombreVariables = nombreVariables;
    }

    public JTextField getNombreContraintes() {
        return nombreContraintes;
    }

    public void setNombreContraintes(JTextField nombreContraintes) {
        this.nombreContraintes = nombreContraintes;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public void setOkButton(JButton okButton) {
        this.okButton = okButton;
    }

    public CardLayout getLayout() {
        return layout;
    }

    public void setLayout(CardLayout layout) {
        this.layout = layout;
    }
}
