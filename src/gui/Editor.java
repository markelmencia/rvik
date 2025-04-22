package gui;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Editor extends JFrame {

    public Editor() {
        setTitle("rvik");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel tAreaPanel = new JPanel();
        tAreaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Editor"));
        JTextArea tArea = new JTextArea(40, 100);
        tArea.setLineWrap(true);
        JScrollPane tAreaSP = new JScrollPane(tArea);
        tAreaSP.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tAreaPanel.add(tAreaSP);

        add(tAreaPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
