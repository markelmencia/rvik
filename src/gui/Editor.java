package gui;

import rv32i.Assembler;
import rv32i.Compiler;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.undo.CannotUndoException;
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
import java.util.BitSet;

public class Editor extends JFrame {

    private File file = null;
    private static CodeTextArea tArea;
    private final JScrollPane tAreaSP;
    private final JButton runButton;
    private final MemoryTable pmTable;
    private final MemoryTable dmTable;

    public Editor() {
        setTitle("rvik");

        // TextArea (CodeTextArea)
        tArea = new CodeTextArea(40, 90);
        tAreaSP = new JScrollPane(tArea);
        tAreaSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tAreaSP.setBorder(new TitledBorder(new EtchedBorder(), "Editor"));

        // Adds custom closing event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        // Data Memory Table
        dmTable = new MemoryTable(110, 10, Compiler.mem);
        JScrollPane dmTableSP = new JScrollPane(dmTable);
        dmTableSP.setBorder(new TitledBorder(new EtchedBorder(), "Data Memory"));

        // Program Memory Table
        pmTable = new MemoryTable(110, 10, Compiler.pm);
        JScrollPane pmTableSP = new JScrollPane(pmTable);
        pmTableSP.setBorder(new TitledBorder(new EtchedBorder(), "Program Memory"));

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Editor", tAreaSP);
        tabbedPane.addTab("Data Memory", dmTableSP);
        tabbedPane.addTab("Program Memory", pmTableSP);

        // JMmenu
        JMenuBar menuBar = getBar();

        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new TitledBorder(new EtchedBorder(), "Simulator"));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JButton assembleButton = new JButton("Assemble");
        assembleButton.addActionListener(actionEvent -> assemble());

        assembleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(assembleButton);
        leftPanel.add(Box.createVerticalStrut(10));

        runButton = new JButton("Run");
        runButton.setEnabled(false);
        runButton.addActionListener(actionEvent -> run());
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(runButton);

        // Register table
        RegisterTable registerTable = new RegisterTable();

        JScrollPane tableSP = new JScrollPane(registerTable);
        tableSP.setBorder(new TitledBorder(new EtchedBorder(), "Register values"));

        setJMenuBar(menuBar);
        add(tabbedPane);
        add(leftPanel, BorderLayout.WEST);
        add(tableSP, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar getBar() {
        JMenuBar menuBar = new JMenuBar();

        // File JMenuItems
        JMenu fileMenu = getFileJMenu();

        // Edit JMenuItems
        JMenu editMenu = getEditJMenu();

        // Help JMenuItems
        JMenu helpMenu = getHelpJMenu();

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JMenu getFileJMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        RvikMenuItem newFile = new RvikMenuItem("New...", KeyEvent.VK_N, KeyEvent.VK_N); // TODO
        newFile.addActionListener(actionEvent -> System.out.println("New"));

        RvikMenuItem openFile = new RvikMenuItem("Open...", KeyEvent.VK_O, KeyEvent.VK_O);
        openFile.addActionListener(actionEvent -> {
            File openedFile = this.openFile();
            if (openedFile != null) {
                file = openedFile;
                setTAreaText(file);
            }
        });

        RvikMenuItem saveFile = new RvikMenuItem("Save", KeyEvent.VK_S, KeyEvent.VK_S);
        saveFile.addActionListener(actionEvent -> saveFile());

        RvikMenuItem saveAsFile = new RvikMenuItem("Save As", 0, KeyEvent.VK_A);
        saveAsFile.addActionListener(actionEvent -> saveAsFile());

        RvikMenuItem preferencesFile = new RvikMenuItem("Preferences...", KeyEvent.VK_P, KeyEvent.VK_P); // TODO
        saveAsFile.setMnemonic(KeyEvent.VK_P);

        RvikMenuItem closeFile = new RvikMenuItem("Close", 0, KeyEvent.VK_C);
        closeFile.addActionListener(actionEvent -> close());

        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(preferencesFile);
        fileMenu.addSeparator();
        fileMenu.add(closeFile);
        return fileMenu;
    }

    private JMenu getEditJMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        RvikMenuItem undoEdit = new RvikMenuItem("Undo", KeyEvent.VK_Z, KeyEvent.VK_U);
        undoEdit.addActionListener(actionEvent -> {
            try {
                tArea.getUndoRedoManager().undo();
            } catch (CannotUndoException ignored) {
            }

        });

        RvikMenuItem redoEdit = new RvikMenuItem("Redo", KeyEvent.VK_Z, KeyEvent.VK_U);
        undoEdit.addActionListener(actionEvent -> {
            try {
                tArea.getUndoRedoManager().redo();
            } catch (CannotUndoException ignored) {
            }

        });


        RvikMenuItem selectAllEdit = new RvikMenuItem("Select All", KeyEvent.VK_A, KeyEvent.VK_S);
        selectAllEdit.addActionListener(actionEvent -> {
            tArea.requestFocusInWindow();
            tArea.selectAll();
        });

        RvikMenuItem cutEdit = new RvikMenuItem("Cut", KeyEvent.VK_X, KeyEvent.VK_C);
        cutEdit.addActionListener(actionEvent -> tArea.cut());

        RvikMenuItem copyEdit = new RvikMenuItem("Copy", KeyEvent.VK_C, KeyEvent.VK_O);
        copyEdit.addActionListener(actionEvent -> tArea.copy());

        RvikMenuItem pasteEdit = new RvikMenuItem("Paste", KeyEvent.VK_V, KeyEvent.VK_P);
        pasteEdit.addActionListener(actionEvent -> tArea.paste());

        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.addSeparator();
        editMenu.add(selectAllEdit);
        editMenu.addSeparator();
        editMenu.add(cutEdit);
        editMenu.add(copyEdit);
        editMenu.add(pasteEdit);

        return editMenu;
    }

    private JMenu getHelpJMenu() {
        JMenu helpMenu = new JMenu("Help");// TODO

        return helpMenu;
    }

    private File openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private void saveFile() {
        if (file != null) {
            try (BufferedWriter fileOut = new BufferedWriter(new FileWriter(file))) {
                tArea.write(fileOut);
                this.file = new File(file.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Could not save the file", "Error", JOptionPane.ERROR_MESSAGE);
            }

            Thread t = new Thread(() -> {
                tAreaSP.setBorder(new TitledBorder(new EtchedBorder(), "Editor (saved)"));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tAreaSP.setBorder(new TitledBorder(new EtchedBorder(), "Editor"));
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

    private void assemble() {
        if (file != null) {
            try {
                saveFile();
                Assembler.assembleFile(file.getPath());
                pmTable.refresh();
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
    }

    public void run() {
        Compiler.pc = 0;
        Compiler.run();
        dmTable.refresh();
    }

    private static void setTAreaText(File file) {
        try {
            tArea.setText(Files.readString(Path.of(file.getPath())));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error when opening file");
        }

    }

    private void close() {
        if (file != null || !tArea.getText().isEmpty()) {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to save before closing?", "Close", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (option == JOptionPane.CLOSED_OPTION) {
                return;
            }
            System.exit(0);
        } else {
            System.exit(0);
        }
    }
}
