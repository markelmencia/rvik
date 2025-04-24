package gui;

import rv32i.Assembler;
import rv32i.Compiler;
import utils.MemorySegment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.BitSet;

public class Editor extends JFrame {

    private File file = null;
    private static JTextArea tArea;
    private DefaultTableModel model;
    private JButton runButton;

    public Editor() {
        setTitle("rvik");
        JPanel tAreaPanel = new JPanel();
        tAreaPanel.setLayout(new BorderLayout());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close(tAreaPanel);
            }
        });

        JTable memoryTable = new JTable(11, 11);
        JScrollPane memoryTableSP = new JScrollPane(memoryTable);
        memoryTableSP.setBorder(new TitledBorder(new EtchedBorder(), "Register values"));
        memoryTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        memoryTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        memoryTable.setFont(memoryTable.getFont().deriveFont(17F));
        memoryTable.setRowHeight(20);
        memoryTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        setHeaders(memoryTable);
        refreshMemoryTable(memoryTable);
        memoryTable.setDefaultRenderer(Object.class, new TableRenderer());

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Text area
        tAreaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Editor"));
        tArea = new JTextArea(40, 90);
        tArea.setLineWrap(true);
        UndoManager undoManager = new UndoManager();
        tArea.getDocument().addUndoableEditListener(undoManager);

        JScrollPane tAreaSP = new JScrollPane(tArea);
        tAreaSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tAreaPanel.add(tAreaSP);

        // JMmenu
        JMenuBar menuBar = new JMenuBar();

        // File JMenuItems
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newFile = new JMenuItem("New...");
        newFile.setMnemonic(KeyEvent.VK_N);
        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        newFile.setAccelerator(ctrlN);
        newFile.addActionListener(actionEvent -> System.out.println("New"));

        JMenuItem openFile = new JMenuItem("Open...");
        openFile.setMnemonic(KeyEvent.VK_O);
        KeyStroke ctrlO = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        openFile.setAccelerator(ctrlO);
        openFile.addActionListener(actionEvent -> {
            File openedFile = this.openFile();
            if (openedFile != null) {
                file = openedFile;
                setTAreaText(file);
            }
        });

        JMenuItem saveFile = new JMenuItem("Save");
        saveFile.setMnemonic(KeyEvent.VK_S);
        KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        saveFile.setAccelerator(ctrlS);
        saveFile.addActionListener(actionEvent -> {
            saveFile(tAreaPanel);
        });

        JMenuItem saveAsFile = new JMenuItem("Save As");
        saveAsFile.setMnemonic(KeyEvent.VK_A);
        saveAsFile.addActionListener(actionEvent -> {
            saveAsFile();
        });

        JMenuItem preferencesFile = new JMenuItem("Preferences...");
        saveAsFile.setMnemonic(KeyEvent.VK_P);

        JMenuItem closeFile = getJMenuItem(tAreaPanel);

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(preferencesFile);
        fileMenu.addSeparator();
        fileMenu.add(closeFile);

        // Edit JMenuItems
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        JMenuItem undoEdit = new JMenuItem("Undo");
        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        undoEdit.setAccelerator(ctrlZ);
        undoEdit.addActionListener(actionEvent -> {
            try {
                undoManager.undo();
            } catch (CannotUndoException ignored) {
            }

        });
        undoEdit.setMnemonic(KeyEvent.VK_U);

        JMenuItem redoEdit = new JMenuItem("Redo");
        redoEdit.addActionListener(actionEvent -> {
            try {
                undoManager.redo();
            } catch (CannotRedoException ignored) {
            }
        });

        KeyStroke ctrlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        redoEdit.setAccelerator(ctrlY);
        redoEdit.setMnemonic(KeyEvent.VK_R);

        JMenuItem selectAllEdit = new JMenuItem("Select All");
        KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        selectAllEdit.setAccelerator(ctrlA);
        selectAllEdit.addActionListener(actionEvent -> {
            tArea.requestFocusInWindow();
            tArea.selectAll();
        });
        selectAllEdit.setMnemonic(KeyEvent.VK_S);
        JMenuItem cutEdit = new JMenuItem("Cut");
        KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        cutEdit.setAccelerator(ctrlX);
        cutEdit.addActionListener(actionEvent -> tArea.cut());
        cutEdit.setMnemonic(KeyEvent.VK_C);
        JMenuItem copyEdit = new JMenuItem("Copy");
        KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        copyEdit.setAccelerator(ctrlC);
        copyEdit.addActionListener(actionEvent -> tArea.copy());
        copyEdit.setMnemonic(KeyEvent.VK_O);
        JMenuItem pasteEdit = new JMenuItem("Paste");
        pasteEdit.setMnemonic(KeyEvent.VK_P);
        KeyStroke ctrlV = KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        pasteEdit.setAccelerator(ctrlV);
        pasteEdit.addActionListener(actionEvent -> tArea.paste());

        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.addSeparator();
        editMenu.add(selectAllEdit);
        editMenu.addSeparator();
        editMenu.add(cutEdit);
        editMenu.add(copyEdit);
        editMenu.add(pasteEdit);

        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Simulator"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JButton assembleButton = new JButton("Assemble");
        assembleButton.addActionListener(actionEvent -> {
            if (file != null) {
                try {
                    saveFile(tAreaPanel);
                    Assembler.assembleFile(file.getPath());
                    runButton.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "The file has been assembled correctly");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "The file could not be assembled", "Error", JOptionPane.ERROR_MESSAGE);
                    // Resets program memory
                    Compiler.pm = new BitSet(65536);
                    runButton.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please save the file before assembling");
            }
        });
        assembleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(assembleButton);
        leftPanel.add(Box.createVerticalStrut(10));

        runButton = new JButton("Run");
        runButton.setEnabled(false);
        runButton.addActionListener(actionEvent -> {
            Compiler.pc = 0;
            Compiler.run();
            refreshTable(model);
            refreshMemoryTable(memoryTable);
        });
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(runButton);

        // Register table
        model = new DefaultTableModel(new String[]{"Register", "Value"}, 0);

        JTable table = new JTable(model);
        JScrollPane tableSP = new JScrollPane(table);
        tableSP.setBorder(new TitledBorder(new EtchedBorder(), "Register values"));
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.setFont(table.getFont().deriveFont(17F));
        table.setRowHeight(20);
        refreshTable(model);

        tabbedPane.addTab("Editor", tAreaPanel);
        tabbedPane.addTab("Data Memory", memoryTableSP);

        setJMenuBar(menuBar);
        add(tabbedPane);
        add(leftPanel, BorderLayout.WEST);
        add(tableSP, BorderLayout.EAST);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuItem getJMenuItem(JPanel tAreaPanel) {
        JMenuItem closeFile = new JMenuItem("Close");
        closeFile.setMnemonic(KeyEvent.VK_C);
        closeFile.addActionListener(actionEvent -> {
            close(tAreaPanel);
        });
        return closeFile;
    }

    private File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private void saveFile(JPanel tAreaPanel) {
        if (file != null) {
            try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(file))) {
                tArea.write(fileOut);
                this.file = new File(file.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not save the file", "Error", JOptionPane.ERROR_MESSAGE);
            }

            Thread t = new Thread(() -> {
                tAreaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Editor (saved)"));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tAreaPanel.setBorder(new TitledBorder(new EtchedBorder(), "Editor"));
            });
            t.start();
        } else {
            saveAsFile();
        }
    }

    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            this.file = file;
            try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(file))) {
                tArea.write(fileOut);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not save the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void setTAreaText(File file) {
        try {
            tArea.setText(Files.readString(Path.of(file.getPath())));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error when opening file");
        }

    }

    private void close(JPanel tAreaPanel) {
        if (file != null || !tArea.getText().isEmpty()) {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to save before closing?", "Close", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveFile(tAreaPanel);
            } else if (option == JOptionPane.CLOSED_OPTION) {
                return;
            }
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    public void refreshTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (int i = 0; i < Compiler.reg.length; i++) {
            model.addRow(new Object[]{i, Compiler.reg[i]});
        }
        model.addRow(new Object[]{"PC", Compiler.pc});
    }


    void setHeaders(JTable table) {
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.setValueAt(((i - 1) * 32), 0, i);
        }
        for (int i = 1; i < table.getRowCount(); i++) {
            table.setValueAt((i - 1) * 10 * 32, i, 0);
        }
    }

    void refreshMemoryTable(JTable table) {
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
}
