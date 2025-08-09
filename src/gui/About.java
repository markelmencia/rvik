package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;

public class About extends JFrame {
    public About() {
        setTitle("rvik - About");
        // Adds custom closing event
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));


        JLabel rvikLabel = new JLabel("rvik");
        rvikLabel.setFont(rvikLabel.getFont().deriveFont(72F));

        JLabel subTitleLabel = new JLabel("RV32i Assembler and Simulator");
        subTitleLabel.setFont(rvikLabel.getFont().deriveFont(26F));
        JLabel markelLabel = new JLabel("By Markel Mencía");
        markelLabel.setFont(rvikLabel.getFont().deriveFont(18F));

        JLabel licenseLabel = new JLabel("This project is licensed under the GNU General Public License v3.0:");
        licenseLabel.setFont(rvikLabel.getFont().deriveFont(14F));
        licenseLabel.setBorder(new EmptyBorder(20, 0, 0, 0));
        JLabel licenseURILabel = getURILabel("See license", "https://www.gnu.org/licenses/gpl-3.0.html");

        JLabel moreInfoLabel = new JLabel("For more information about the proyect, read README.md in the project repository:");
        moreInfoLabel.setFont(rvikLabel.getFont().deriveFont(14F));
        moreInfoLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JLabel githubURILabel = getURILabel("GitHub repository", "https://github.com/markelmencia/rvik");

        // Button Panel
        JPanel closeButtonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(_ -> dispose());
        closeButtonPanel.add(closeButton);
        closeButtonPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        mainPanel.add(rvikLabel);
        mainPanel.add(subTitleLabel);
        mainPanel.add(markelLabel);
        mainPanel.add(licenseLabel);
        mainPanel.add(licenseURILabel);
        mainPanel.add(moreInfoLabel);
        mainPanel.add(githubURILabel);

        add(mainPanel);
        add(closeButtonPanel, BorderLayout.SOUTH);

        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static JLabel getURILabel(String text, String uri) {
        JLabel linkLabel = new JLabel("<html><a href=''>" + text + "</a></html>");
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(uri));
                } catch (Exception ex) {
                    System.err.println("Could not open the following URL: " + uri);
                }
            }
        });
        return linkLabel;
    }
}
