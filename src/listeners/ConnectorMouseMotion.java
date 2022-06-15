package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import chartobjects.ChartObject;
import chartobjects.Connector;
import main.undoredo.UndoRedoNode;

public class ConnectorMouseMotion implements MouseMotionListener {
    private Connector connector;

    public ConnectorMouseMotion(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.isControlDown()) {
            // Check if just stared resizing, if so then add a new undoable-stack-frame
            if(!connector.getChartObject().isResizing()) {
                connector.getChartObject().getModel().addUndoableStackFrame(
                    new HashSet<>(){{add(connector.getChartObject());}},
                    UndoRedoNode.REZISED
                );
            }
            dragToResize(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        connector.setCursor(e);
    }

    private void dragToResize(MouseEvent e) {
        Connector c = connector;
        ChartObject co = connector.getChartObject();

        int x = c.getX();
        int y = c.getY();
        int w = co.getWidth();
        int h = co.getHeight();
        int dx = e.getX() - c.getStartPoint().x;
        int dy = e.getY() - c.getStartPoint().y;

        final int minSize = 60;

        switch(c.getPosition()) {
            case ENE:
            case E:
            case ESE:
                if(w + dx > minSize) {
                    c.setLocation(x + dx, y);
                    co.setBounds(co.getX(), co.getY(), co.getWidth() + dx, co.getHeight());
                }
                break;
            case NNW:
            case N:
            case NNE:
                if(h - dy > minSize) {
                    c.setLocation(x, y + dy);
                    co.setBounds(co.getX(), co.getY() + dy, co.getWidth(), co.getHeight() - dy);
                }
                break;
            case NE:
                if(w + dx > minSize && h - dy > minSize) {
                    c.setLocation(x + dx, y + dy);
                    co.setBounds(co.getX(), co.getY() + dy, co.getWidth() + dx, co.getHeight() - dy);
                }
                break;
            case NW:
                if(w - dx > minSize && h - dy > minSize) {
                    c.setLocation(x + dx, y + dy);
                    co.setBounds(co.getX() + dx, co.getY() + dy, co.getWidth() - dx, co.getHeight() - dy);
                }
                break;
            case SSW:
            case S:
            case SSE:
                if(h + dy > minSize) {
                    c.setLocation(x, y + dy);
                    co.setBounds(co.getX(), co.getY(), co.getWidth(), co.getHeight() + dy);
                }
                break;
            case SE:
                if(w + dx > minSize && h + dy > minSize) {
                    c.setLocation(x + dx, y + dy);
                    co.setBounds(co.getX(), co.getY(), co.getWidth() + dx, co.getHeight() + dy);
                }
                break;
            case SW:
                if(w - dx > minSize && h + dy > minSize) {
                    c.setLocation(x + dx, y + dy);
                    co.setBounds(co.getX() + dx, co.getY(), co.getWidth() - dx, co.getHeight() + dy);
                }
                break;
            case WNW:
            case W:
            case WSW:
                if(w - dx > minSize) {
                    c.setLocation(x + dx, y);
                    co.setBounds(co.getX() + dx, co.getY(), co.getWidth() - dx, co.getHeight());
                }
                break;
            default:
        }
        connector.getChartObject().setResizing(true);
        co.resize();
    }
}