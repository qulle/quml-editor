package listeners;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
import staticmanagers.CommandManager;
import main.DrawingSurface;

public class DrawingSurfaceMouseMotion implements MouseMotionListener {
    private DrawingSurface drawingSurface;

    public DrawingSurfaceMouseMotion(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        drawingSurface.setCursorPoint(e.getPoint());

        if(drawingSurface.getModel().getStartConnector() != null) {
            drawingSurface.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(CommandManager.getCommand(KeyEvent.VK_SPACE) || SwingUtilities.isMiddleMouseButton(e)) {
            // When dragging the entire drawingSurface
            drawingSurface.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

            Point origin = drawingSurface.getClickedPoint();
            double zoomFactor = drawingSurface.getWorkSpace().getZoomPanel().getScale();

            if(origin != null) {
                JViewport viewPort = drawingSurface.getWorkSpace().getScrollPane().getViewport();
                if(viewPort != null) {
                    int deltaX = (int)((origin.x - e.getX()) * zoomFactor);
                    int deltaY = (int)((origin.y - e.getY()) * zoomFactor);

                    Rectangle view = viewPort.getViewRect();
                    view.x += deltaX;
                    view.y += deltaY;

                    drawingSurface.scrollRectToVisible(view);
                }
            }
        }else if(SwingUtilities.isLeftMouseButton(e)) {
            // When dragging to select objects
            int x1 = (int)drawingSurface.getClickedPoint().getX();
            int y1 = (int)drawingSurface.getClickedPoint().getY();
            int x2 = (int)e.getPoint().getX();
            int y2 = (int)e.getPoint().getY();

            drawingSurface.getSelection().setBounds(
                    x1 > x2 ? x2 : x1,
                    y1 > y2 ? y2 : y1,
                    Math.abs(x1 - x2),
                    Math.abs(y1 - y2)
            );

            drawingSurface.getModel().selectBoxes(drawingSurface.getSelection());
            drawingSurface.repaint();
        }
    }
}