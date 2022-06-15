package menus;

import lines.ChartLine;
import relations.ConnectorSymbol;
import staticmanagers.IconManager;
import javax.swing.*;

public class ContextChartLine extends JPopupMenu {
    private JMenu subMenuRelation = new JMenu("Relation");
    private JMenu subMenuLineType = new JMenu("Line type");
    private JMenuItem menuItemBent;
    private JMenuItem menuItemSemi;
    private JMenuItem menuItemStraight;

    public ContextChartLine(ChartLine cl) {

        JMenuItem menuItemDelete = new JMenuItem("Delete line", IconManager.getIcon("16/trash_bin"));
        menuItemDelete.addActionListener(e -> cl.getModel().deleteLine(cl));

        JMenuItem menuItemEdit = new JMenuItem("Edit", IconManager.getIcon("16/edit"));
        menuItemEdit.addActionListener(e -> cl.openEditWindow());

        JMenuItem menuItemNone = new JMenuItem("None", IconManager.getIcon("16/none"));
        menuItemNone.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.NONE));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemAssociation = new JMenuItem("Association", IconManager.getIcon("16/association"));
        menuItemAssociation.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.ASSOCIATION));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemInheritance = new JMenuItem("Generalization", IconManager.getIcon("16/inheritance"));
        menuItemInheritance.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.INHERITANCE));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemImplementation = new JMenuItem("Interface Realization", IconManager.getIcon("16/implementing"));
        menuItemImplementation.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.IMPLEMENTATION));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemDependency = new JMenuItem("Dependency", IconManager.getIcon("16/dependency"));
        menuItemDependency.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.DEPENDENCY));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemAggregation = new JMenuItem("Aggregation", IconManager.getIcon("16/aggregation"));
        menuItemAggregation.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.AGGREGATION));
            cl.getModel().getDrawingSurface().repaint();
        });

        JMenuItem menuItemComposition = new JMenuItem("Composition", IconManager.getIcon("16/composition"));
        menuItemComposition.addActionListener(e -> {
            cl.setStartConnectorSymbol(cl.getModel().getConnectorSymbol(ConnectorSymbol.COMPOSITION));
            cl.getModel().getDrawingSurface().repaint();
        });

        menuItemStraight = new JMenuItem("Straight", IconManager.getIcon("16/straight"));
        menuItemStraight.addActionListener(e -> {
            cl.setLineType(ChartLine.Type.STRAIGHT);
            cl.getModel().update();
        });

        menuItemSemi = new JMenuItem("Semi", IconManager.getIcon("16/semi"));
        menuItemSemi.addActionListener(e -> {
            cl.setLineType(ChartLine.Type.SEMI);
            cl.getModel().update();
        });

        menuItemBent = new JMenuItem("Bent", IconManager.getIcon("16/bent"));
        menuItemBent.addActionListener(e -> {
            cl.setLineType(ChartLine.Type.BENT);
            cl.getModel().update();
        });

        subMenuRelation.add(menuItemNone);
        subMenuRelation.add(menuItemAssociation);
        subMenuRelation.add(menuItemInheritance);
        subMenuRelation.add(menuItemImplementation);
        subMenuRelation.add(menuItemDependency);
        subMenuRelation.add(menuItemAggregation);
        subMenuRelation.add(menuItemComposition);

        subMenuLineType.add(menuItemStraight);
        subMenuLineType.add(menuItemSemi);
        subMenuLineType.add(menuItemBent);

        add(menuItemEdit);
        add(subMenuLineType);
        add(subMenuRelation);
        add(menuItemDelete);
    }

    public JMenuItem getSubMenuRelation() {
        return subMenuRelation;
    }

    public JMenuItem getSubMenuLineType() {
        return subMenuLineType;
    }

    public JMenuItem getMenuItemSemi() {
        return menuItemSemi;
    }

    public JMenuItem getMenuItemBent() {
        return menuItemBent;
    }

    public JMenuItem getMenuItemStraight() {
        return menuItemStraight;
    }
}