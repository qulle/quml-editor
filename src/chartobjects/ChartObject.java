package chartobjects;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import lines.ChartLine;
import listeners.ChartObjectKey;
import listeners.ChartObjectMouse;
import listeners.ChartObjectMouseMotion;
import main.Model;
import main.undoredo.UndoRedo;
import main.undoredo.UndoRedoNode;
import menus.ContextChartObject;

public abstract class ChartObject extends JPanel implements UndoRedo {
    private static final long serialVersionUID = 1L;
    private transient ContextChartObject contextMenu;

    private Model model;
    private Map<Connector.Position, Connector> connectors = new HashMap<>();
    private Set<ChartLine> chartLines = new HashSet<>();

    private Point startPoint = new Point(0, 0);
    private boolean resizing = false;

    public abstract ChartObject copy();
    public abstract void doCommand(KeyEvent e);
    public abstract void openEditWindow();
    public abstract Color getColor();

    public ChartObject(Model model) {
        this.model = model;
        setFocusable(true);
        addTransientComponents();
    }

    public void addConnectors() {
        addConnector(new Connector(this, Connector.Position.N));
        addConnector(new Connector(this, Connector.Position.NNE));
        addConnector(new Connector(this, Connector.Position.NNW));
        addConnector(new Connector(this, Connector.Position.NE));
        addConnector(new Connector(this, Connector.Position.ENE));
        addConnector(new Connector(this, Connector.Position.E));
        addConnector(new Connector(this, Connector.Position.ESE));
        addConnector(new Connector(this, Connector.Position.SE));
        addConnector(new Connector(this, Connector.Position.SSE));
        addConnector(new Connector(this, Connector.Position.S));
        addConnector(new Connector(this, Connector.Position.SSW));
        addConnector(new Connector(this, Connector.Position.SW));
        addConnector(new Connector(this, Connector.Position.WSW));
        addConnector(new Connector(this, Connector.Position.W));
        addConnector(new Connector(this, Connector.Position.WNW));
        addConnector(new Connector(this, Connector.Position.NW));
    }

    public Model getModel() {
        return model;
    }

    public void addTransientComponents() {
        contextMenu = new ContextChartObject(this);
    }

    public void addListeners() {
        addMouseListener(new ChartObjectMouse(this));
        addMouseMotionListener(new ChartObjectMouseMotion(this));
        addKeyListener(new ChartObjectKey(this));

        for(Connector c : connectors.values()) {
            c.addListeners();
        }
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
        if(node.isSet(UndoRedoNode.MOVED) || node.isSet(UndoRedoNode.REZISED)) {
            setBounds(node.getBounds());
            model.setSelected(this, true);
        }else if(node.isSet(UndoRedoNode.ADDED)) {
            model.delete(this, false);
        }else if(node.isSet(UndoRedoNode.DELETED)) {
            model.add(this, false);
            model.setSelected(this, true);
        }
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds();
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        setConnectorLocation();
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
        setConnectorLocation();
    }

    public void addInternalComponents() {
        for(Connector c : connectors.values()) {
            model.getDrawingSurface().add(c, 0);
        }
    }

    public void removeInternalComponents() {
        for(Connector c : connectors.values()) {
            model.getDrawingSurface().remove(c);
        }
    }

    public void resize() {
        revalidate();
        setConnectorLocation();
        model.updateLines();
        model.getDrawingSurface().repaint();
    }

    public void setConnectorLocation() {
        for(Connector c : connectors.values()) {
            c.setLocation();
        }
    }

    public void toggleConnectors(boolean flag) {
        for(Connector c : connectors.values()) {
            c.setVisible(flag);
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }

    void addConnector(Connector c) {
        connectors.put(c.getPosition(), c);
    }

    public boolean isResizing() {
        return resizing;
    }
    
    public void setResizing(boolean value) {
        resizing = value;
    }

    public boolean isBeingSelected(Rectangle selection) {
        return selection.intersects(getBounds());
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public Set<ChartLine> getLines() {
        return chartLines;
    }

    public Connector getConnector(Connector.Position position) {
        return connectors.get(position);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public void addLine(ChartLine chartLine) {
        chartLines.add(chartLine);
    }

    public void deleteLine(ChartLine chartLine) {
        chartLines.remove(chartLine);
    }

    public void dragMove(int dx, int dy) {
        setLocation(getX() + dx, getY() + dy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}