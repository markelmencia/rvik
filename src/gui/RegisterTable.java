package gui;

import rv32i.Compiler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RegisterTable extends JTable {
    DefaultTableModel registerTableModel;

    public RegisterTable() {
        registerTableModel = new DefaultTableModel(new String[]{"Register", "Value"}, 0);
        setModel(registerTableModel);

        getColumnModel().getColumn(0).setPreferredWidth(20);
        getColumnModel().getColumn(1).setPreferredWidth(110);
        setFont(this.getFont().deriveFont(17F));
        setRowHeight(19);
        // Initialization
        init();
    }

    public void init() {
        registerTableModel.setRowCount(0);
        for (int i = 0; i < Compiler.reg.length; i++) {
            registerTableModel.addRow(new Object[]{i, String.format("0x%08X", Compiler.reg[i])});
        }
        registerTableModel.addRow(new Object[]{"PC", String.format("0x%08X", Compiler.pc)});
    }
}
