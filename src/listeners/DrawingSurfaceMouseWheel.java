package listeners;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import main.DrawingSurface;

public class DrawingSurfaceMouseWheel implements MouseWheelListener {
    private DrawingSurface drawingSurface;

    public DrawingSurfaceMouseWheel(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // When zooming using the mouseWheel
        if(e.isControlDown()) {
            if(e.getWheelRotation() < 0) {
                drawingSurface.getWorkSpace().getZoomPanel().zoomIn();
            }else if(e.getWheelRotation() > 0) {
                drawingSurface.getWorkSpace().getZoomPanel().zoomOut();
            }

            drawingSurface.getWorkSpace().getTabManager().getSideBar().updateZoomLabel();
        }
    }
}