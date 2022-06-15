package menus;

import chartobjects.ChartObject;
import staticmanagers.CopyPasteManager;
import staticmanagers.IconManager;
import javax.swing.*;

public class ContextChartObject extends JPopupMenu {
    public ContextChartObject(ChartObject co) {
        JMenuItem menuItemEdit = new JMenuItem("Edit", IconManager.getIcon("16/edit"));
        JMenuItem menuItemCopy = new JMenuItem("Copy", IconManager.getIcon("16/copy"));
        JMenuItem menuItemRemove = new JMenuItem("Delete box(es)", IconManager.getIcon("16/trash_bin"));

        menuItemEdit.addActionListener(e -> co.openEditWindow());

        menuItemCopy.addActionListener(e -> {
            co.getModel().setSelected(co, true);
            CopyPasteManager.copy(co.getModel());

            if(!co.getModel().isSingleSelected()) {
                co.getModel().setSelected(co, false);
            }
        });

        menuItemRemove.addActionListener(e -> co.getModel().delete(co));

        add(menuItemEdit);
        add(menuItemCopy);
        add(menuItemRemove);
    }
}