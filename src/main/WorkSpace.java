package main;

import transformzooming.ZoomPanel;
import java.awt.BorderLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

public class WorkSpace extends JPanel {
    private static final long serialVersionUID = 1L;
    static final int WIDTH  = 5000;
    static final int HEIGHT = 5000;
    
    private TabManager tabManager;
    private DrawingSurface drawingSurface;
    private ZoomPanel zoomPanel;
    private JScrollPane scrollPane;

    WorkSpace(TabManager tabManager) {
        this.tabManager = tabManager;
        setLayout(new BorderLayout());

        // Workspace --> ScrollPane --> ZoomPanel --> DrawingSurface --> Model
        drawingSurface = new DrawingSurface(this);
        zoomPanel = new ZoomPanel(drawingSurface, WIDTH, HEIGHT);
        scrollPane = new JScrollPane(
            zoomPanel,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        // Disable arrow keys to prevent scroll-move the scrollPane when arrow-move chartObjects
        WorkSpace.disableArrowKeys(scrollPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT));

        add(scrollPane, BorderLayout.CENTER);
    }

    private static void disableArrowKeys(InputMap im) {
        String[] keystrokeNames = {"UP", "DOWN", "LEFT", "RIGHT",};

        for(String key : keystrokeNames) {
            im.put(KeyStroke.getKeyStroke(key), "none");
        }
    }

    public TabManager getTabManager() {
        return tabManager;
    }

    public DrawingSurface getDrawingSurface() {
        return drawingSurface;
    }

    public ZoomPanel getZoomPanel() {
        return zoomPanel;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}