package gui;

import rv32i.Compiler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RegisterTable extends JTable {
    DefaultTableModel registerTableModel;

    public RegisterTable() {
        registerTableModel = new DefaultTableModel(new String[]{"Register", "Value"}, 0);
        setModel(registerTableModel);
        getColumnModel().getColumn(0).setPreferredWidth(30);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        setFont(this.getFont().deriveFont(17F));
        setRowHeight(20);
        // Initialization
        refresh();
    }

    public void refresh() {
        registerTableModel.setRowCount(0);
        for (int i = 0; i < Compiler.reg.length; i++) {
            registerTableModel.addRow(new Object[]{i, Compiler.reg[i]});
        }
        registerTableModel.addRow(new Object[]{"PC", Compiler.pc});
    }
}
