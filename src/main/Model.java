package main;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import lines.ChartLine;
import main.undoredo.UndoRedo;
import main.undoredo.UndoRedoNode;
import relations.*;
import staticmanagers.ColorManager;
import chartobjects.*;

public class Model implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient DrawingSurface drawingSurface;
    private transient Stack<ArrayList<UndoRedoNode>> undoable = new Stack<>();
    private transient Stack<ArrayList<UndoRedoNode>> redoable = new Stack<>();
    private transient String filePath = null;

    private Set<ChartObject> chartObjects = new LinkedHashSet<>();
    private Set<ChartObject> selectedObjects = new HashSet<>();
    private Set<ChartLine> lines = new HashSet<>();
    private Connector[] makeLine = new Connector[2];

    private boolean connectorsVisible = true;
    private boolean gridVisible = true;
    private boolean dragging = false;

    private static final Map<Integer, ConnectorSymbol> connectorSymbols = new HashMap<>() {{
        put(ConnectorSymbol.NONE, new None());
        put(ConnectorSymbol.DEPENDENCY, new Dependency());
        put(ConnectorSymbol.AGGREGATION, new Aggregation());
        put(ConnectorSymbol.COMPOSITION, new Composition());
        put(ConnectorSymbol.ASSOCIATION, new Association());
        put(ConnectorSymbol.INHERITANCE, new Inheritance());
        put(ConnectorSymbol.IMPLEMENTATION, new Implementation());
    }};

    Model(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public boolean getDragging() {
        return dragging;
    }

    public ConnectorSymbol[] getConnectorSymbols() {
        return connectorSymbols.values().toArray(new ConnectorSymbol[0]);
    }

    public ConnectorSymbol getConnectorSymbol(ConnectorSymbol c) {
        return getConnectorSymbol(c.getType());
    }

    public ConnectorSymbol getConnectorSymbol(int index) {
        return connectorSymbols.get(index);
    }

    public Connector getStartConnector() {
        return makeLine[0];
    }

    void setDrawingSurface(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    public DrawingSurface getDrawingSurface() {
        return drawingSurface;
    }

    public Set<ChartLine> getLines() {
        return lines;
    }

    boolean isGridVisible() {
        return gridVisible;
    }

    public Set<ChartObject> getSelectedObjects() {
        return selectedObjects;
    }

    public boolean isSingleSelected() {
        return selectedObjects.size() == 1;
    }

    public boolean isConnectorsVisible() {
        return connectorsVisible;
    }

    void addTransientComponents() {
        undoable = new Stack<>();
        redoable = new Stack<>();

        for(ChartObject co : chartObjects) {
            co.addTransientComponents();
        }

        for(ChartLine cl : lines) {
            cl.addTransientComponents();
        }
    }

    void addListeners() {
        for(ChartObject co : chartObjects) {
            co.addListeners();
        }

        for(ChartLine cl : lines) {
            cl.addLabelListeners();
        }
    }

    public void undo() {
        if(!undoable.isEmpty()) {
            // Deselect all objects
            deSelectAll();

            // prevState now holds a ref to a list of objects to be undone
            ArrayList<UndoRedoNode> prevState = undoable.pop();
            ArrayList<UndoRedoNode> currentState = new ArrayList<>(prevState.size());

            // Save current state
            for(UndoRedoNode node : prevState) {
                currentState.add(new UndoRedoNode(
                    node.getItem(),
                    new Rectangle(node.getItem().getBounds()),
                    node.getAndToggleEvents()
                ));
            }

            // Undo to the prevState
            for(UndoRedoNode node : prevState) {
                node.getItem().undo(node);
            }

            // Push currentState to redoable stack
            redoable.push(currentState);

            // Update the model
            update();
        }
    }

    public void redo() {
        if(!redoable.isEmpty()) {
            // Deselect all objects
            deSelectAll();

            // prevState now holds a ref to a list of objects to be redone
            ArrayList<UndoRedoNode> prevState = redoable.pop();
            ArrayList<UndoRedoNode> currentState = new ArrayList<>(prevState.size());

            // Save current state
            for(UndoRedoNode node : prevState) {
                currentState.add(new UndoRedoNode(
                    node.getItem(),
                    new Rectangle(node.getItem().getBounds()),
                    node.getAndToggleEvents()
                ));
            }

            // Redo to prevState
            for(UndoRedoNode node : prevState) {
                node.getItem().redo(node);
            }

            // Push currentState to undoable stack
            undoable.push(currentState);

            // Update the model
            update();
        }
    }

    public void addUndoableStackFrame(Set<? extends UndoRedo> items, int ... indexes) {
        ArrayList<UndoRedoNode> list = new ArrayList<>();

        // Created the list of items
        for(UndoRedo item : items) {
            list.add(new UndoRedoNode(
                item,
                new Rectangle(item.getBounds()),
                indexes
            ));
        }

        // Push list of items to the undo stack
        undoable.push(list);

        // Clear redo stack, preventing unwanted model states
        redoable.clear();
    }

    public void add(ChartObject co) {
        add(co, true);
    }

    public void add(ChartObject co, boolean undoable) {
        chartObjects.add(co);
        updateAfterAdded(co);

        if(undoable) {
            addUndoableStackFrame(
                new HashSet<>(){{add(co);}},
                UndoRedoNode.ADDED
            );
        }
    }

    public void delete(ChartObject co) {
        delete(co, true);
    }

    public void delete(ChartObject co, boolean undoable) {
        setSelected(co, true);
        deleteSelected(undoable);
    }

    void deleteSelected() {
        deleteSelected(true);
    }

    private void deleteSelected(boolean undoable) {

        if(undoable) {
            // Get all lines to be deleted and store them and their objects in a undoable list
            Set<ChartLine> linesToDelete = new HashSet<>();
            for(ChartObject co : selectedObjects) {
                linesToDelete.addAll(co.getLines());
            }

            // Make new undoable stack frame, LinkedHasSet (insertion-order) important,
            // Needs to add all objects before the lines
            addUndoableStackFrame(new LinkedHashSet<>(){{
                addAll(selectedObjects);
                addAll(linesToDelete);
            }},
                UndoRedoNode.DELETED
            );
        }

        // Delete all objects
        Set<ChartObject> copySelectedObjects = new HashSet<>(selectedObjects);
        for(ChartObject co : copySelectedObjects) {
            // Remove all lines connected to co
            cleanUpConnections(co);

            // Remove co from model
            chartObjects.remove(co);
            selectedObjects.remove(co);

            // Remove co from drawingSurface panel
            co.removeInternalComponents();
            drawingSurface.remove(co);
        }

        drawingSurface.requestFocus();
    }

    // Help method for when deleting chartObjects and their lines
    private void cleanUpConnections(ChartObject co) {
        Set<ChartLine> copyLines = new HashSet<>(co.getLines());

        for(ChartLine chartLine : copyLines) {
            deleteLine(chartLine, false);
        }

        // If the box that is deleted has a partially connected new line
        if(makeLine[0] != null && makeLine[0].isOwnedBy(co)) {
            clearMakeLine();
        }
    }

    public void clear() {
        chartObjects.clear();
        lines.clear();

        // Clear a potential partially created line
        clearMakeLine();

        // Set filepath to null and update the tab
        filePath = null;

        TabManager tm = drawingSurface.getWorkSpace().getTabManager();
        int tabIndex = tm.getSelectedIndex();
        tm.setTitleAt(tabIndex, "Drawing " + tabIndex);

        // Clear and update the drawingsurface
        drawingSurface.removeAll();
        update();
    }

    private void updateAfterAdded(ChartObject co) {
        drawingSurface.add(co, 0);
        co.addInternalComponents();

        update();
    }

    void updateAfterOpened() {
        drawingSurface.removeAll();

        // Add all objects from opened model
        for(ChartObject co : chartObjects) {
            drawingSurface.add(co);
            co.addInternalComponents();
        }

        update();

        // Add lines internal components
        for(ChartLine line : lines) {
            line.addInternalComponents();
        }
    }

    public void update() {
        updateLines();
        drawingSurface.revalidate();
        drawingSurface.repaint();
    }

    public void updateLines() {
        for(ChartLine line : lines) {
            line.update();
        }
    }

    public void toggleConnectors() {
        connectorsVisible = !connectorsVisible;
        for(ChartObject co : chartObjects) {
            co.toggleConnectors(connectorsVisible);
        }
    }

    public void toggleGrid() {
        gridVisible = !gridVisible;
        drawingSurface.repaint();
    }

    void clearMakeLine() {
        makeLine[0] = null;
        makeLine[1] = null;
    }

    // Selections
    public void selectBoxes(Rectangle selection) {
        for(ChartObject co : chartObjects) {
            if(co.isBeingSelected(selection)) {
                setSelected(co, true);
            }else {
                setSelected(co, false);
            }
        }
    }

    public void setSelected(ChartObject chartObject, boolean flag) {
        if(flag) {
            selectedObjects.add(chartObject);
            chartObject.setBackground(ColorManager.BOX_SELECTED_BACKGROUND.getColor());
        }else {
            selectedObjects.remove(chartObject);
            chartObject.setBackground(chartObject.getColor());
        }

        drawingSurface.repaint();
    }

    public boolean isSelected(ChartObject co) {
        return selectedObjects.contains(co);
    }

    public void deSelectAll() {
        for(ChartObject co : selectedObjects) {
            co.setBackground(co.getColor());
        }
        selectedObjects.clear();
    }

    void selectAll() {
        for(ChartObject co : chartObjects) {
            setSelected(co, true);
        }
    }

    public void moveSelected(int dx, int dy) {
        for(ChartObject co : selectedObjects) {
            co.dragMove(dx, dy);
        }
        updateLines();
        drawingSurface.repaint();
    }

    // Calculate active image area when exporting as png
    int[] calcImageArea() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = 0, maxY = 0;
        final int padding = 70;

        if(chartObjects.isEmpty()) {
            return new int[] {0, 0, WorkSpace.WIDTH, WorkSpace.HEIGHT};
        }

        // Find bounds
        for(ChartObject co : chartObjects) {
            if(co.getX() < minX) {
                minX = co.getX();
                minX = (minX - padding < 0) ? 0 : minX - padding;
            }

            if(co.getY() < minY) {
                minY = co.getY();
                minY = (minY - padding < 0) ? 0 : minY - padding;
            }

            if(co.getX() + co.getWidth() > maxX) {
                maxX = co.getX() + co.getWidth();
                maxX = (maxX + padding > WorkSpace.WIDTH) ? WorkSpace.WIDTH : maxX + padding;
            }

            if(co.getY() + co.getHeight() > maxY) {
                maxY = co.getY() + co.getHeight();
                maxY = (maxY + padding > WorkSpace.HEIGHT) ? WorkSpace.HEIGHT : maxY + padding;
            }
        }

        return new int[] {minX, minY, maxX, maxY};
    }

    public void manageConnection(Connector connector) {
        if(makeLine[0] == null) {
            makeLine[0] = connector;
        }else if(makeLine[1] == null) {
            // Check if trying to connect same connector as start-connector
            if(!makeLine[0].equals(connector)) {
                makeLine[1] = connector;

                boolean lineAlreadyExists = false;

                for(ChartLine chartLine : lines) {
                    if(chartLine.hasPairOfConnectors(makeLine)) {
                        lineAlreadyExists = true;
                    }
                }

                if(!lineAlreadyExists) {
                    ChartLine line = new ChartLine(this, makeLine[0], makeLine[1]);
                    addLine(line);
                }

                clearMakeLine();
                drawingSurface.repaint();
            }
        }
    }

    public void addLine(ChartLine cl) {
        addLine(cl, true);
    }

    public void addLine(ChartLine cl, boolean undoable) {
        if(undoable) {
            addUndoableStackFrame(
                new HashSet<>(){{add(cl);}},
                UndoRedoNode.ADDED
            );
        }

        // Add line to model
        lines.add(cl);

        // Add Labels to drawingSurface
        cl.addInternalComponents();

        // Add line to both parent boxes
        cl.getStartChartObject().addLine(cl);
        cl.getEndChartObject().addLine(cl);
    }

    public void deleteLine(ChartLine cl) {
        deleteLine(cl, true);
    }

    public void deleteLine(ChartLine cl, boolean undoable) {
        if(undoable) {
            cl.getModel().addUndoableStackFrame(
                new HashSet<>(){{add(cl);}},
                UndoRedoNode.DELETED
            );
        }

        // Remove line from model
        lines.remove(cl);

        // Remove line from both parent boxes
        cl.getStartChartObject().deleteLine(cl);
        cl.getEndChartObject().deleteLine(cl);

        // Remove Labels from drawingSurface
        cl.removeInternalComponents();
    }

    void render(Graphics2D g2) {
        g2.setColor(ColorManager.LINE.getColor());

        for(ChartLine chartLine : lines) {
            chartLine.drawLine(g2);
        }

        g2.setStroke(new BasicStroke(1));
    }
}