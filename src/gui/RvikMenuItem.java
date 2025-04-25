package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class RvikMenuItem extends JMenuItem {
    public RvikMenuItem(String name, int shortcut, int mnemonic) {
        setMnemonic(mnemonic);
        // 0 means no accelerator
        if (shortcut != 0) {
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        }
        addActionListener(actionEvent -> System.out.println("New"));
    }
}
