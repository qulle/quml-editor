package menus;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import main.Editor;
import main.TabManager;
import staticmanagers.ColorManager;
import staticmanagers.CopyPasteManager;
import staticmanagers.FileManager;
import staticmanagers.IconManager;

public class SideBar extends JPanel {
    private static final long serialVersionUID = 1L;
    private TabManager tabManager;
    private JLabel zoomFactor = new JLabel();

    public SideBar(TabManager tabManager) {
        this.tabManager = tabManager;
        setPreferredSize(new Dimension(40, getPreferredSize().height));
        setBackground(ColorManager.SIDEBAR_BACKGROUND.getColor());

        add(new Button("New", IconManager.getIcon("32/new_document"), tabManager::addTab));

        add(new Button("Close", IconManager.getIcon("32/close_document"), () -> {
            if(JOptionPane.showConfirmDialog(Editor.window, "Close drawing?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                tabManager.removeTab();
            }
        }));

        add(new Button("Undo", IconManager.getIcon("32/undo"), () ->
            tabManager.getCurrentModel().undo()
        ));

        add(new Button("Redo", IconManager.getIcon("32/redo"), () ->
            tabManager.getCurrentModel().redo()
        ));

        add(new Button("Save", IconManager.getIcon("32/save"), () ->
            FileManager.save(tabManager.getCurrentModel())
        ));

        add(new Button("Open", IconManager.getIcon("32/open"), () ->
            tabManager.getCurrentWorkSpace().getDrawingSurface().openFile()
        ));

        add(new Button("Copy", IconManager.getIcon("32/copy"), () ->
            CopyPasteManager.copy(tabManager.getCurrentModel())
        ));

        add(new Button("Paste", IconManager.getIcon("32/paste"), () ->
            CopyPasteManager.paste(tabManager.getCurrentModel())
        ));

        add(new Button("Export as PNG", IconManager.getIcon("32/export"), () ->
            tabManager.getCurrentWorkSpace().getDrawingSurface().exportAsImage()
        ));

        add(new Button("Clear", IconManager.getIcon("32/trash_bin"), () -> {
            if(JOptionPane.showConfirmDialog (Editor.window, "Clear all boxes and lines?","Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                tabManager.getCurrentModel().clear();
            }
        }));

        add(new Button("Zoom-in", IconManager.getIcon("32/zoom_in"), () -> {
            tabManager.getCurrentWorkSpace().getZoomPanel().zoomIn();
            updateZoomLabel();
        }));

        add(new Button("Zoom-out", IconManager.getIcon("32/zoom_out"), () -> {
            tabManager.getCurrentWorkSpace().getZoomPanel().zoomOut();
            updateZoomLabel();
        }));

        updateZoomLabel();
        zoomFactor.setFont(new Font("Serif", Font.PLAIN, 10));
        add(zoomFactor);
    }

    public void updateZoomLabel() {
        double percent = tabManager.getCurrentWorkSpace().getZoomPanel().getScale() * 100;
        zoomFactor.setText(new DecimalFormat("#").format(percent) + "%");
    }

    private final class Button extends JPanel {
        private static final long serialVersionUID = 1L;

        private Button(String toolTipText, ImageIcon icon, Runnable r) {
            setPreferredSize(new Dimension(50, 40));
            setToolTipText(toolTipText);
            add(new JLabel(icon));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    r.run();
                }

                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}
                
                @Override
                public void mouseExited(MouseEvent e) {}
            });
        }
    }
}