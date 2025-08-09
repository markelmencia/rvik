package gui;

import utils.MemorySegment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.BitSet;

public class MemoryTable extends JTable {

    public MemoryTable(int numRows, int numColumns, BitSet memory) {
        DefaultTableModel model = new DefaultTableModel(numRows + 1, numColumns + 1) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
        getColumnModel().getColumn(0).setPreferredWidth(80);
        setFont(getFont().deriveFont(17F));
        setRowHeight(20);
        setDefaultRenderer(Object.class, new MemoryTableRenderer());
        setTableHeader(null);
        // Initializes values
        setHeaders();
        init(memory);
    }

    public void setHeaders() {
        for (int i = 1; i < this.getColumnCount(); i++) {
            this.setValueAt(((i - 1) * 32), 0, i);
        }
        for (int i = 1; i < this.getRowCount(); i++) {
            this.setValueAt((i - 1) * 10 * 32, i, 0);
        }
    }

    // Initializes the values of the table according to the provided BitSet
    public void init(BitSet memory) {
        int k = 0;
        for (int i = 1; i < this.getRowCount(); i++) {
            for (int j = 1; j < this.getColumnCount(); j++) {
                int value = new MemorySegment(k, memory).getValue();
                if (value == 0) {
                    this.setValueAt(0, i, j);
                } else {
                    this.setValueAt(String.format("0x%08X", value), i, j);
                }
                k += 32;
            }
        }
    }
}
