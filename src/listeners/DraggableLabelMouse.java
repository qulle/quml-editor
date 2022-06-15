package listeners;

import lines.DraggableLabel;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DraggableLabelMouse implements MouseListener {
    private DraggableLabel dl;

    public DraggableLabelMouse(DraggableLabel dl) {
        this.dl = dl;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
            dl.getChartLine().openEditWindow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dl.setClickedPoint(e.getPoint());
        dl.setDragging(true);
        dl.getChartLine().getModel().getDrawingSurface().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dl.setCursor(Cursor.getDefaultCursor());
        
        int[] anchorCoordinates = dl.getAnchorPointCoordinates();
        dl.setXOffset(anchorCoordinates[0] - dl.getX());
        dl.setYOffset(anchorCoordinates[1] - dl.getY());

        dl.setDragging(false);
        dl.getChartLine().getModel().getDrawingSurface().repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}