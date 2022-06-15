package main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import listeners.DrawingSurfaceKey;
import listeners.DrawingSurfaceMouse;
import listeners.DrawingSurfaceMouseMotion;
import listeners.DrawingSurfaceMouseWheel;
import chartobjects.Connector;
import menus.ContextDrawingSurface;
import staticmanagers.*;

public class DrawingSurface extends JPanel {
    private static final long serialVersionUID = 1L;

    private transient WorkSpace workSpace;

    private Model model;
    private ContextDrawingSurface contextMenu;
    private Point clickedPoint = new Point(0, 0);
    private Point cursorPoint = new Point(0, 0);
    private Rectangle selection = new Rectangle();

    DrawingSurface(WorkSpace workSpace) {
        this.workSpace = workSpace;
        model = new Model(this);
        contextMenu = new ContextDrawingSurface(this);

        setLayout(null);
        setAutoscrolls(true);
        setPreferredSize(new Dimension(WorkSpace.WIDTH, WorkSpace.HEIGHT));
        setBackground(ColorManager.DRAWING_SURFACE_BACKGROUND.getColor());

        addMouseListener(new DrawingSurfaceMouse(this));
        addMouseMotionListener(new DrawingSurfaceMouseMotion(this));
        addMouseWheelListener(new DrawingSurfaceMouseWheel(this));
        addKeyListener(new DrawingSurfaceKey(this));
    }

    public void doCommand(KeyEvent e) {
        if(e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            model.redo();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            model.undo();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
            CopyPasteManager.copy(model);
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
            CopyPasteManager.paste(model);
        }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            model.clearMakeLine();
            model.update();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            model.selectAll();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
            FileManager.save(model);
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_O) {
            openFile();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_N || e.isControlDown() && e.getKeyCode() == KeyEvent.VK_T) {
            workSpace.getTabManager().addTab();
        }else if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {
            if(JOptionPane.showConfirmDialog(Editor.window, "Close drawing?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                workSpace.getTabManager().removeTab();
            }
        }else if(e.getKeyCode() == KeyEvent.VK_DELETE) {
            model.deleteSelected();
        }else if(e.getKeyCode() == KeyEvent.VK_UP) {
            model.moveSelected(0, e.isControlDown() ? -1 : -5);
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            model.moveSelected(0, e.isControlDown() ? 1 : 5);
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            model.moveSelected(e.isControlDown() ? 1 : 5, 0);
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            model.moveSelected(e.isControlDown() ? -1 : -5, 0);
        }else {
            CommandManager.setCommand(e);
        }
    }

    public WorkSpace getWorkSpace() {
        return workSpace;
    }

    public Model getModel() {
        return model;
    }

    public JPopupMenu getContextMenu() {
        return contextMenu;
    }

    public void setClickedPoint(Point p) {
        clickedPoint = p;
    }

    public Point getClickedPoint() {
        return clickedPoint;
    }

    public void setCursorPoint(Point p) {
        cursorPoint = p;
    }

    public Rectangle getSelection() {
        return selection;
    }

    // Help-method to open file
    public void openFile() {
        Model tempModel = FileManager.loadFromFile();
        if(tempModel != null) {
            model = tempModel;
            model.setDrawingSurface(this);
            model.addTransientComponents();
            model.addListeners();
            model.updateAfterOpened();
            updateTabLabel();
        }
    }

    public void updateTabLabel() {
        workSpace.getTabManager().updateTabLabel(model.getFilePath());
    }

    public void exportAsImage() {
        final int minX = 0;
        final int minY = 1;
        final int maxX = 2;
        final int maxY = 3;

        // Find size of active chart area
        int[] imageArea = model.calcImageArea();

        BufferedImage image = new BufferedImage(imageArea[maxX], imageArea[maxY], BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Paint drawingSurface to the buffered image
        paint(g2);

        // Cut out part of image that contains the chart
        image = image.getSubimage(
            imageArea[minX],
            imageArea[minY],
            imageArea[maxX] - imageArea[minX],
            imageArea[maxY] - imageArea[minY]
        );

        // Save the BufferedImage to file
        FileManager.saveImage(image);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Grid
        if(model.isGridVisible()) {
            g2.setColor(ColorManager.GRID.getColor());
            final int sideLength = 20;
            int nRowCount = getHeight() / sideLength;
            int currentY = sideLength;
            for(int i = 0; i < nRowCount; i++) {
                g2.drawLine(0, currentY, getWidth(), currentY);
                currentY += sideLength;
            }

            int nColumnCount = getWidth() / sideLength;
            int currentX = sideLength;
            for(int i = 0; i < nColumnCount; i++) {
                g2.drawLine(currentX, 0, currentX, getHeight());
                currentX += sideLength;
            }
        }

        // Selection
        g2.setColor(ColorManager.SELECTION_BACKGROUND.getColor());
        g2.fillRect(
            (int)selection.getX(),
            (int)selection.getY(),
            (int)selection.getWidth(),
            (int)selection.getHeight()
        );

        g2.setColor(ColorManager.SELECTION_BORDER.getColor());
        g2.drawRect(
            (int)selection.getX(),
            (int)selection.getY(),
            (int)selection.getWidth(),
            (int)selection.getHeight()
        );

        // Help-line when making new connection
        Connector connector = model.getStartConnector();
        if(connector != null) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setStroke(new BasicStroke(2));

            g2.setColor(ColorManager.LINE.getColor());
            g2.drawLine(connector.getCenterX(), connector.getCenterY(), cursorPoint.x, cursorPoint.y);
        }

        // Model
        model.render(g2);
    }
}