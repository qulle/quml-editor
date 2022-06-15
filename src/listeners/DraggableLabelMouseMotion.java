package listeners;

import lines.DraggableLabel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DraggableLabelMouseMotion implements MouseMotionListener {
    private DraggableLabel dl;

    public DraggableLabelMouseMotion(DraggableLabel dl) {
        this.dl = dl;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dl.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        int dx = e.getX() - dl.getClickedPoint().x;
        int dy = e.getY() - dl.getClickedPoint().y;

        dl.setLocation(dl.getX() + dx, dl.getY() + dy);
        dl.getChartLine().getModel().getDrawingSurface().repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}