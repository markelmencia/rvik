package gui;

import rv32i.Compiler;
import utils.MemorySegment;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MemoryTable extends JFrame {

    private DefaultTableModel model;

    public MemoryTable() {

        JTable table = new JTable(11, 11);
        JScrollPane tableSP = new JScrollPane(table);
        tableSP.setBorder(new TitledBorder(new EtchedBorder(), "Register values"));
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.setFont(table.getFont().deriveFont(17F));
        table.setRowHeight(20);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        setHeaders(table);
        refreshTable(table);
        table.setDefaultRenderer(Object.class, new TableRenderer());

        add(table);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    void setHeaders(JTable table) {
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.setValueAt(((i - 1) * 32), 0, i);
        }
        for (int i = 1; i < table.getRowCount(); i++) {
            table.setValueAt((i - 1) * 10 * 32, i, 0);
        }
    }

    void refreshTable(JTable table) {
        int k = 0;
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                table.setValueAt(new MemorySegment(k).getValue(), i, j);
                k += 32;
            }
        }
    }

    private class TableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setOpaque(true);
            if (row == 0 && column == 0) {
                value = "Address";
            }
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);



            if (row == 0 || column == 0) {
                c.setBackground(Color.GRAY.brighter());
            } else {
                c.setBackground(Color.WHITE);
            }
            return this;
        }
    }

    public static void main(String[] args) {
        new MemoryTable();
    }
}


