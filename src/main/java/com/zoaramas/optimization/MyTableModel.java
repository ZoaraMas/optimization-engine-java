package com.zoaramas.optimization;


import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.zoaramas.optimization.logic.*;


public class MyTableModel extends AbstractTableModel {
    private Fraction[][] table;
    private ArrayList<Variable> columnNames;
    private String[] rowNames;

    public MyTableModel(Fraction[][] table, ArrayList<Variable> columnNames, String[] rowNames) {
        this.table = Service.clone(table);
        this.columnNames = columnNames;
        this.rowNames = rowNames;
    }

    public MyTableModel(Fraction[][] table, ArrayList<Variable> columnNames) {
        this.table = Service.clone(table);
        this.columnNames = columnNames;
        // this.rowNames = rowNames;
    }

    @Override
    public String getColumnName(int column) {
        if (columnNames != null && column < columnNames.size()) {
            return columnNames.get(column).toString();
        }
        return super.getColumnName(column);
    }

    @Override
    public int getRowCount() {
        return this.table.length;
    }

    @Override
    public int getColumnCount() {
        return this.table[0].length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return table[rowIndex][columnIndex];
    }

}
