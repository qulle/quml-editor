package listeners;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import chartobjects.ChartObject;

public class ChartObjectMouse implements MouseListener {
    private ChartObject chartObject;

    public ChartObjectMouse(ChartObject chartObject) {
        this.chartObject = chartObject;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() == 2) {
            chartObject.openEditWindow();
        }

        // Toggle the selection of the clicked object
        if(e.isControlDown()) {
            chartObject.getModel().setSelected(
                chartObject,
                !chartObject.getModel().isSelected(chartObject)
            );
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            showContextMenu(e);
        }

        if(!e.isControlDown()) {
            chartObject.getModel().setSelected(chartObject, true);
        }

        chartObject.requestFocus();
        chartObject.setStartPoint(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        chartObject.setCursor(Cursor.getDefaultCursor());

        if(chartObject.getModel().isSingleSelected() && !e.isControlDown()) {
            chartObject.getModel().setSelected(chartObject, false);
        }

        chartObject.getModel().setDragging(false);
        chartObject.getModel().getDrawingSurface().repaint();
    }

    private void showContextMenu(MouseEvent e) {
        // Make zoom adjustment to the location of the context menu
        double zoomFactor = chartObject.getModel().getDrawingSurface().getWorkSpace().getZoomPanel().getScale();

        int x = (int)((chartObject.getX() + e.getX()) * zoomFactor);
        int y = (int)((chartObject.getY() + e.getY()) * zoomFactor);

        chartObject.getContextMenu().show(chartObject.getModel().getDrawingSurface(), x, y);
    }
}