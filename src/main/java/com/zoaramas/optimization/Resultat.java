package com.zoaramas.optimization;


import javax.swing.*;

import com.zoaramas.optimization.logic.Fraction;
import com.zoaramas.optimization.logic.Result;

import java.awt.*;
import java.util.ArrayList;

public class Resultat extends JPanel {
    private CardLayout layout;
    private Main win;

    // private ArrayList<JTable> listeTable;
    private JLabel resultString;

    public Resultat(Main window, CardLayout cardLayout) throws Exception {
        // Result result = Service.getSystemeTemp();
        this.win = window;
        this.layout = cardLayout;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.setAutoscrolls(true);
        this.setAlignmentY(Component.TOP_ALIGNMENT);
    }

    public void createTables(Result result) throws Exception {
        ArrayList<Fraction[][]> histo = result.getHistorique();
        int indexListeVariable = 0;
        for (int i = 0; i < histo.size(); i++) {
            if (i == result.getIndexChangementPhase()) {
                add(new JLabel("CHANGEMENT EN PHASE 2"));
                indexListeVariable++;
            }
            MyTableModel tableModel = new MyTableModel(histo.get(i),
                    result.getHistoriqueVariable().get(indexListeVariable));
            JTable table = new JTable(tableModel);
            
            // Set preferred viewport size for the table
            table.setPreferredScrollableViewportSize(
                new Dimension(table.getPreferredSize().width, 
                            table.getRowHeight() * Math.min(table.getRowCount(), 10))
            );
            table.setFillsViewportHeight(true);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 200)); // Adjust as needed
            
            add(new JLabel(result.getHistoriqueBase().get(i)));
            add(scrollPane);
            add(new JLabel("-------------------------------------------------------"));
        }
        resultString = new JLabel(result.getResultString());
        add(resultString);
    }

    public void createTables(Result result, Result resultEntier) throws Exception {
        ArrayList<Fraction[][]> histo = result.getHistorique();
        int indexListeVariable = 0;
        for (int i = 0; i < histo.size(); i++) {
            if (i == result.getIndexChangementPhase()) {
                add(new JLabel("CHANGEMENT EN PHASE 2"));
                indexListeVariable++;
            }
            MyTableModel tableModel = new MyTableModel(histo.get(i),
                    result.getHistoriqueVariable().get(indexListeVariable));
            JTable table = new JTable(tableModel);
            
            // Add these lines:
            table.setPreferredScrollableViewportSize(
                new Dimension(table.getPreferredSize().width, 
                            table.getRowHeight() * Math.min(table.getRowCount(), 10))
            );
            table.setFillsViewportHeight(true);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 200)); // Add this line
            
            add(new JLabel(result.getHistoriqueBase().get(i)));
            add(scrollPane);
            add(new JLabel("-------------------------------------------------------"));
        }
        resultString = new JLabel(result.getResultString() + "\n entiere: " + resultEntier.getResultString());
        System.out.println("resultat createTables avec entier = ");
        System.out.println(resultString);
        add(resultString);
    }
}
