package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import staticmanagers.ColorManager;
import chartobjects.Connector;

public class ConnectorMouse implements MouseListener {
    private Connector connector;

    public ConnectorMouse(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        connector.getChartObject().getModel().manageConnection(connector);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        connector.setBackground(ColorManager.CONNECTOR_HOVER_BACKGROUND.getColor());
        connector.setCursor(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        connector.setBackground(ColorManager.CONNECTOR_BACKGROUND.getColor());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        connector.requestFocus();
        connector.setStartPoint(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        connector.getChartObject().setResizing(false);
        connector.getChartObject().resize();
    }
}