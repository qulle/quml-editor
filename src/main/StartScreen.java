package main;

import staticmanagers.IconManager;
import staticmanagers.WindowManager;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartScreen extends JPanel {
    static JFrame window;
    public static void main(String[] args){
        IconManager.loadImages();

        SwingUtilities.invokeLater(StartScreen::new);
    }

    private StartScreen() {
        JFrame window = new JFrame("QUML Editor");
        StartScreen.window = window;
        window.getContentPane().setPreferredSize(new Dimension(800, 533));
        window.pack();

        WindowManager.centerWindow(window);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setIconImage(IconManager.getImage("32/logo"));
        window.setResizable(false);

        setLayout(null);

        JLabel lblNewDocument = new JLabel("New document");
        lblNewDocument.setSize(lblNewDocument.getPreferredSize());
        lblNewDocument.setLocation(120, 300);
        lblNewDocument.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblNewDocument.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Editor().setVisible(true);
                window.dispose();
            }
        });

        add(lblNewDocument);

        window.getContentPane().add(this);
        window.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(IconManager.getImage("start-screen"), 0, 0, null);
    }
}