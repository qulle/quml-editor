package menus;

import chartobjects.CircularInterface;
import chartobjects.ClassBox;
import chartobjects.Comment;
import main.DrawingSurface;
import main.Editor;
import staticmanagers.CopyPasteManager;
import staticmanagers.FileManager;
import staticmanagers.IconManager;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class ContextDrawingSurface extends JPopupMenu {

    public ContextDrawingSurface(DrawingSurface ds) {
        JMenuItem menuItemNew = new JMenuItem("New", IconManager.getIcon("16/new_document"));
        menuItemNew.addActionListener(e -> ds.getWorkSpace().getTabManager().addTab());

        JMenuItem menuItemRemove = new JMenuItem("Close", IconManager.getIcon("16/close_document"));
        menuItemRemove.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog (Editor.window, "Close drawing?","Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                ds.getWorkSpace().getTabManager().removeTab();
            }
        });

        JMenu subMenuInsert = new JMenu("Insert");

        JMenuItem menuItemNewBox = new JMenuItem("Class box", IconManager.getIcon("16/class"));
        menuItemNewBox.addActionListener(e -> ds.getModel().add(new ClassBox(ds.getModel(), ds.getClickedPoint())));

        subMenuInsert.add(menuItemNewBox);

        JMenuItem menuItemNewCircle = new JMenuItem("Circular interface", IconManager.getIcon("16/interface"));
        menuItemNewCircle.addActionListener(e -> ds.getModel().add(new CircularInterface(ds.getModel(), ds.getClickedPoint())));

        JMenuItem menuItemNewComment = new JMenuItem("Comment", IconManager.getIcon("16/class"));
        menuItemNewComment.addActionListener(e -> ds.getModel().add(new Comment(ds.getModel(), ds.getClickedPoint())));

        subMenuInsert.add(menuItemNewCircle);
        subMenuInsert.add(menuItemNewComment);

        JMenuItem menuItemToggleConnectors = new JMenuItem("Toggle connectors");
        menuItemToggleConnectors.addActionListener(e -> ds.getModel().toggleConnectors());

        JMenuItem menuItemToggleGrid = new JMenuItem("Toggle grid", IconManager.getIcon("16/grid"));
        menuItemToggleGrid.addActionListener(e -> ds.getModel().toggleGrid());

        JMenuItem menuItemClearAll = new JMenuItem("Clear all", IconManager.getIcon("16/trash_bin"));
        menuItemClearAll.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog (Editor.window, "Clear all boxes and lines?","Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                ds.getModel().clear();
            }
        });

        JMenuItem menuItemCopy = new JMenuItem("Copy", IconManager.getIcon("16/copy"));
        menuItemCopy.addActionListener(e -> CopyPasteManager.copy(ds.getModel()));

        JMenuItem menuItemPaste = new JMenuItem("Paste", IconManager.getIcon("16/paste"));
        menuItemPaste.addActionListener(e -> CopyPasteManager.paste(ds.getModel()));

        JMenuItem menuItemSave = new JMenuItem("Save", IconManager.getIcon("16/save"));
        menuItemSave.addActionListener(e -> FileManager.save(ds.getModel()));

        JMenuItem menuItemSaveAs = new JMenuItem("Save As", IconManager.getIcon("16/save"));
        menuItemSaveAs.addActionListener(e -> FileManager.saveAs(ds.getModel()));

        JMenuItem menuItemOpen = new JMenuItem("Open", IconManager.getIcon("16/open"));
        menuItemOpen.addActionListener(e -> ds.openFile());

        JMenuItem menuItemExport = new JMenuItem("Export png", IconManager.getIcon("16/export"));
        menuItemExport.addActionListener(e -> ds.exportAsImage());

        add(menuItemNew);
        add(menuItemRemove);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(subMenuInsert);
        add(menuItemToggleConnectors);
        add(menuItemToggleGrid);
        add(menuItemClearAll);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemCopy);
        add(menuItemPaste);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemSave);
        add(menuItemSaveAs);
        add(menuItemOpen);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(menuItemExport);
    }
}