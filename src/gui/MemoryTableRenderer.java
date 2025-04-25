package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MemoryTableRenderer extends DefaultTableCellRenderer {
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
