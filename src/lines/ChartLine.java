package lines;

import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.io.Serializable;
import javax.swing.ImageIcon;
import chartobjects.ChartObject;
import chartobjects.Connector;
import main.undoredo.UndoRedoNode;
import menus.ContextChartLine;
import relations.ConnectorSymbol;
import staticmanagers.ColorManager;
import staticmanagers.IconManager;
import main.Model;
import main.undoredo.UndoRedo;

/**
 * ChartLine represents the lines between objects.
 */
public class ChartLine implements Serializable, UndoRedo {
    public enum Type {
        STRAIGHT("16/straight"),
        SEMI("16/semi"),
        BENT("16/bent");

        private final ImageIcon icon;

        Type(String iconName) {
            this.icon = IconManager.getIcon(iconName);
        }

        public ImageIcon getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    private static final long serialVersionUID = 1L;
    private static Rectangle undoHelp = new Rectangle();
    private static LineWindow lineWindow = new LineWindow();

    private Model model;
    private transient ContextChartLine contextMenu;

    private Connector startConnector;
    private Connector endConnector;
    private ConnectorSymbol startConnectorSymbol;
    private ConnectorSymbol endConnectorSymbol;
    private Type lineType;
    private Line2D[] path = new Line2D[1];

    private DraggableLabel[] lineLabels = new DraggableLabel[] {
        new DraggableLabel(this),
        new DraggableLabel(this)
    };

    public ChartLine(Model model, Connector startConnector, Connector endConnector) {
        this.model = model;
        this.startConnector = startConnector;
        this.endConnector = endConnector;

        startConnectorSymbol = model.getConnectorSymbol(ConnectorSymbol.NONE);
        endConnectorSymbol = model.getConnectorSymbol(ConnectorSymbol.NONE);

        lineLabels[0].setAnchorPoint(startConnector);
        lineLabels[1].setAnchorPoint(endConnector);

        setLineType(Type.BENT);

        addTransientComponents();
        update();
    }

    private ChartLine(Model model, Connector startConnector, Connector endConnector, ConnectorSymbol symbol, Type lineType, DraggableLabel label0, DraggableLabel label1) {
        this(model, startConnector, endConnector);

        this.startConnectorSymbol = symbol;
        this.lineType = lineType;
        this.lineLabels[0] = label0;
        this.lineLabels[1] = label1;
    }

    public ChartLine copy() {
        return new ChartLine(
            model,
            startConnector,
            endConnector,
            startConnectorSymbol,
            lineType,
            lineLabels[0].copy(),
            lineLabels[1].copy()
        );
    }

    public void addTransientComponents() {
        contextMenu = new ContextChartLine(this);
    }

    @Override
    public void undo(UndoRedoNode node) {
        undoRedoHelp(node);
    }

    @Override
    public void redo(UndoRedoNode node) {
        undoRedoHelp(node);
    }

    private void undoRedoHelp(UndoRedoNode node) {
        if(node.isSet(UndoRedoNode.ADDED)) {
            model.deleteLine(this, false);
        }else if(node.isSet(UndoRedoNode.DELETED)) {
            model.addLine(this, false);
        }
    }

    @Override
    public Rectangle getBounds() {return ChartLine.undoHelp;}

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Line2D[] getPath() {
        return path;
    }

    Type getLineType() {
        return lineType;
    }

    public boolean is(Type lineType) {
        return this.lineType == lineType;
    }

    void setLineLabel(int index, String text) {
        lineLabels[index].setText(text);
    }

    DraggableLabel getLineLabel(int index) {
        return lineLabels[index];
    }

    public Connector getStartConnector() {
        return startConnector;
    }

    public Connector getEndConnector() {
        return endConnector;
    }

    // Shortcut to prevent long lines
    public ChartObject getStartChartObject() {
        return startConnector.getChartObject();
    }

    public ChartObject getEndChartObject() {
        return endConnector.getChartObject();
    }

    public void setStartConnectorSymbol(ConnectorSymbol cs) {
        startConnectorSymbol = cs;
    }
    
    ConnectorSymbol getStartConnectorSymbol() {
        return startConnectorSymbol;
    }

    private void setLabelLocation() {
        DraggableLabel dl;

        // Left label
        dl = lineLabels[0];
        dl.setSize(dl.getPreferredSize());
        dl.updateLocation();

        // Right label
        dl = lineLabels[1];
        dl.setSize(dl.getPreferredSize());
        dl.updateLocation();
    }

    public void addLabelListeners() {
        for(DraggableLabel dl : lineLabels) {
            dl.addListeners();
        }
    }

    public void setLineType(Type lineType) {
        // Default
        this.lineType = lineType;

        // Exceptions
        if(lineType == Type.BENT && (startConnector.isCorner() || endConnector.isCorner())) {
            this.lineType = Type.STRAIGHT;
        }

        // If chartLine is between same chartObject it needs to be a bent line
        if(startConnector.getChartObject().equals(endConnector.getChartObject())) {
            this.lineType = Type.BENT;
        }
    }

    public void openEditWindow() {
        lineWindow.updateWindow(this);
        lineWindow.setVisible(true);
    }

    public void showContextMenu(MouseEvent e) {
        // Set current selected relation icon and lineType icon
        contextMenu.getSubMenuRelation().setIcon(startConnectorSymbol.getIcon());
        contextMenu.getSubMenuLineType().setIcon(lineType.getIcon());

        // Check if items need to be disabled
        if(startConnector.isCorner() || endConnector.isCorner()) {
            contextMenu.getMenuItemBent().setEnabled(false);
        }

        if(startConnector.getChartObject().equals(endConnector.getChartObject())) {
            contextMenu.getMenuItemSemi().setEnabled(false);
            contextMenu.getMenuItemStraight().setEnabled(false);
        }

        // Show menu, calc zoom-factor offset
        double zoomFactor = model.getDrawingSurface().getWorkSpace().getZoomPanel().getScale();
        int x = (int)(e.getX() * zoomFactor);
        int y = (int)(e.getY() * zoomFactor);

        contextMenu.show(e.getComponent(), x, y);
    }

    public void update() {
        path = PathFinder.findPath(this);
        setLabelLocation();
    }

    public void setStartConnector(Connector connector) {
        startConnector = connector;
        lineLabels[0].setAnchorPoint(connector);
    }

    public void setEndConnector(Connector connector) {
        endConnector = connector;
        lineLabels[1].setAnchorPoint(connector);
    }

    void swap() {
        Connector temp = startConnector;
        startConnector = endConnector;
        endConnector = temp;
    }

    public void addInternalComponents() {
        for(DraggableLabel dl : lineLabels) {
            model.getDrawingSurface().add(dl);
        }
        setLabelLocation();
    }

    public void removeInternalComponents() {
        model.getDrawingSurface().remove(lineLabels[0]);
        model.getDrawingSurface().remove(lineLabels[1]);
    }

    public boolean hasPairOfConnectors(Connector[] makeLine) {
        return(startConnector.equals(makeLine[0]) && endConnector.equals(makeLine[1])) ||
                (startConnector.equals(makeLine[1]) && endConnector.equals(makeLine[0]));
    }

    public void drawLine(Graphics2D g2) {
        g2.setColor(ColorManager.LINE.getColor());

        if(startConnectorSymbol.is(ConnectorSymbol.IMPLEMENTATION) || startConnectorSymbol.is(ConnectorSymbol.DEPENDENCY)) {
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        }else {
            g2.setStroke(new BasicStroke(2));
        }

        // Draw all partial lines
        for(Line2D line : path) {
            g2.draw(line);
        }

        // Draw the connector-relations-symbols
        startConnectorSymbol.draw(g2, this);
        endConnectorSymbol.draw(g2, this);

        // Draggable label
        for(DraggableLabel dl : lineLabels) {
            dl.render(g2);
        }
    }
}