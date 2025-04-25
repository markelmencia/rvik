package gui;

import utils.MemorySegment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.BitSet;

public class MemoryTable extends JTable {

    private final BitSet memory;

    public MemoryTable(int numRows, int numColumns, BitSet memory) {
        this.memory = memory;
        DefaultTableModel model = new DefaultTableModel(numRows + 1, numColumns + 1);
        setModel(model);
        getColumnModel().getColumn(0).setPreferredWidth(80);
        setFont(getFont().deriveFont(17F));
        setRowHeight(20);
        setDefaultRenderer(Object.class, new MemoryTableRenderer());
        setTableHeader(null);
        // Initializes values
        setHeaders();
        refresh();
    }

    public void setHeaders() {
        for (int i = 1; i < this.getColumnCount(); i++) {
            this.setValueAt(((i - 1) * 32), 0, i);
        }
        for (int i = 1; i < this.getRowCount(); i++) {
            this.setValueAt((i - 1) * 10 * 32, i, 0);
        }
    }

    public void refresh() {
        int k = 0;
        for (int i = 1; i < this.getRowCount(); i++) {
            for (int j = 1; j < this.getColumnCount(); j++) {
                this.setValueAt(new MemorySegment(k, memory).getValue(), i, j);
                k += 32;
            }
        }
    }
}
