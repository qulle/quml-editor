package listeners;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import javax.swing.SwingUtilities;
import chartobjects.CircularInterface;
import chartobjects.ClassBox;
import main.DrawingSurface;
import lines.ChartLine;

/**
 * MouseListener for DrawingSurface
 */
public class DrawingSurfaceMouse implements MouseListener {
    private DrawingSurface drawingSurface;

    public DrawingSurfaceMouse(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawingSurface.setCursor(Cursor.getDefaultCursor());
        drawingSurface.getSelection().setBounds(0, 0, 0, 0);
        drawingSurface.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawingSurface.requestFocus();
        drawingSurface.setClickedPoint(e.getPoint());

        if(SwingUtilities.isRightMouseButton(e)) {
            ChartLine chartLine = clickedOnLine(e);
            if(chartLine != null) {
                chartLine.showContextMenu(e);
            }else {
                showContextMenu(e);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!e.isControlDown()) {
            drawingSurface.getModel().deSelectAll();
        }

        if(SwingUtilities.isMiddleMouseButton(e) && e.isControlDown()) {
            drawingSurface.getWorkSpace().getZoomPanel().setScale(1.0);
            drawingSurface.getWorkSpace().getTabManager().getSideBar().updateZoomLabel();
        }else if(SwingUtilities.isMiddleMouseButton(e)) {
            drawingSurface.getModel().toggleConnectors();
        }else if(SwingUtilities.isLeftMouseButton(e)) {
            if(e.getClickCount() == 2) {
                if(e.isControlDown()) {
                    drawingSurface.getModel().add(new CircularInterface(
                        drawingSurface.getModel(),
                        e.getPoint())
                    );
                }else {
                    drawingSurface.getModel().add(new ClassBox(
                        drawingSurface.getModel(),
                        e.getPoint())
                    );
                }
            }
        }
    }

    private void showContextMenu(MouseEvent e) {
        // Make zoom adjustment to the location of the context menu
        double zoomFactor = drawingSurface.getWorkSpace().getZoomPanel().getScale();

        int x = (int)(e.getX() * zoomFactor);
        int y = (int)(e.getY() * zoomFactor);

        drawingSurface.getContextMenu().show(e.getComponent(), x, y);
    }

    private ChartLine clickedOnLine(MouseEvent e) {
        final int minDistance = 5;
        Point p = e.getPoint();

        for(ChartLine chartLine : drawingSurface.getModel().getLines()) {
            Line2D[] path = chartLine.getPath();
            for(Line2D line : path) {
                if(line.ptSegDist(p) <= minDistance) {
                    return chartLine;
                }
            }
        }

        return null;
    }
}