package main;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import menus.SideBar;
import staticmanagers.IconManager;

public class Editor extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Referee parentComponent JOptionPane to Editor.window
    public static JFrame window;
    private SideBar sideBar;

    Editor() {
        super("QUML Editor", StartScreen.window.getGraphicsConfiguration());
        Editor.window = this;
        getContentPane().setPreferredSize(new Dimension(1300, 700));
        pack();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIconImage(IconManager.getImage("32/logo"));

        // Create and add the content
        TabManager tabManager = new TabManager(this);
        sideBar = new SideBar(tabManager);

        getContentPane().add(tabManager, BorderLayout.CENTER);
        getContentPane().add(sideBar, BorderLayout.EAST);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if(JOptionPane.showConfirmDialog (Editor.this, "Close window?","Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                    System.exit(0);
                }
            }
        });
    }

    SideBar getSideBar() {
        return sideBar;
    }
}