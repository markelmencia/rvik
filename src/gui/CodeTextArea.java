package gui;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class CodeTextArea extends JTextArea {

    private final UndoManager undoRedoManager;

    public UndoManager getUndoRedoManager() {
        return undoRedoManager;
    }

    public CodeTextArea(int rows, int columns) {
        setRows(rows);
        setColumns(columns);
        setLineWrap(true);

        undoRedoManager = new UndoManager();
        getDocument().addUndoableEditListener(undoRedoManager);
    }
}
