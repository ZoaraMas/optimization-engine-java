package com.zoaramas.optimization;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Formulaire1 formulaire1;
    private Formulaire2 formulaire2;
    private Resultat pageResultat;

    public Main() throws Exception {
        setTitle("SIMPLEX");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // setSize(1000, 800);
        setLocationRelativeTo(null);

        this.cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        this.formulaire1 = new Formulaire1(this, cardLayout);
        this.formulaire2 = new Formulaire2(this, cardLayout);
        this.pageResultat = new Resultat(this, cardLayout);

        JScrollPane scrollPane = new JScrollPane(pageResultat);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Pour un scroll plus fluide
        
        mainPanel.add(formulaire1, "Form1");
        mainPanel.add(formulaire2, "Form2");
        // mainPanel.add(pageResultat, "pageResultat");
        mainPanel.add(scrollPane, "pageResultat");
        add(mainPanel);
        cardLayout.show(mainPanel, "form1");
    }

    public void setupForm2(int nombreVariable, int nombreContrainte, String maxMin) {
        this.formulaire2.setup(nombreVariable, nombreContrainte, maxMin);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main window;
            try {
                window = new Main();
                window.setVisible(true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public Formulaire1 getFormulaire1() {
        return formulaire1;
    }

    public void setFormulaire1(Formulaire1 formulaire1) {
        this.formulaire1 = formulaire1;
    }

    public Formulaire2 getFormulaire2() {
        return formulaire2;
    }

    public void setFormulaire2(Formulaire2 formulaire2) {
        this.formulaire2 = formulaire2;
    }

    public Resultat getPageResultat() {
        return pageResultat;
    }

    public void setPageResultat(Resultat pageResultat) {
        this.pageResultat = pageResultat;
    }

}