package gui;

import javax.swing.*;
import java.awt.*;

public class RvikMenuItem extends JMenuItem {
    public RvikMenuItem(String name, int shortcut, int mnemonic) {
        setText(name);
        setMnemonic(mnemonic);
        // 0 means no accelerator
        if (shortcut != 0) {
            setAccelerator(KeyStroke.getKeyStroke(shortcut, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        }
    }
}
