package chartobjects;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;
import listeners.ConnectorKey;
import listeners.ConnectorMouse;
import listeners.ConnectorMouseMotion;
import staticmanagers.ColorManager;

public class Connector extends JPanel {
    public enum Position {
        N(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
        NNE(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)),
        NE(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)),
        ENE(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
        E(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
        ESE(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR)),
        SE(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)),
        SSE(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
        S(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
        SSW(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)),
        SW(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)),
        WSW(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
        W(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
        WNW(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)),
        NW(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)),
        NNW(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));

        private final Cursor cursor;

        Position(Cursor cursor) {
            this.cursor = cursor;
        }

        public Cursor getCursor() {
            return cursor;
        }
    }

    private static final long serialVersionUID = 1L;
    private ChartObject chartObject;
    private Cursor resizeCursor;
    private Position position;
    private Point startPoint = new Point(0, 0);

    public Connector(ChartObject chartObject, Position position) {
        this.chartObject = chartObject;
        this.resizeCursor = position.getCursor();
        this.position = position;

        setBackground(ColorManager.CONNECTOR_BACKGROUND.getColor());
        setBorder(new LineBorder(ColorManager.CONNECTOR_BORDER.getColor(), 1));

        final int initWidth = 12;
        final int initHeight = 12;

        setSize(initWidth, initHeight);
        setVisible(chartObject.getModel().isConnectorsVisible());
        setLocation();
        setFocusable(true);
    }

    void addListeners() {
        addMouseListener(new ConnectorMouse(this));
        addMouseMotionListener(new ConnectorMouseMotion(this));
        addKeyListener(new ConnectorKey(this));
    }

    public ChartObject getChartObject() {
        return chartObject;
    }

    public Position getPosition() {
        return position;
    }

    public boolean is(Position position) {
        return this.position == position;
    }

    public boolean isOwnedBy(ChartObject co) {
        return chartObject.equals(co);
    }

    public void setCursor(MouseEvent e) {
        setCursor(e.isControlDown() ? resizeCursor : Cursor.getDefaultCursor());
    }

    public int getCenterX() {
        return getX() + getWidth() / 2;
    }

    public int getCenterY() {
        return getY() + getHeight() / 2;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    void setLocation() {
        final int intersectionFix = 1;

        switch(position) {
            case N:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 2) - (getWidth() / 2), chartObject.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NNE:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 4) * 3 - (getWidth() / 2), chartObject.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NE:
                setLocation(chartObject.getX() + chartObject.getWidth() - (getWidth() / 2) + intersectionFix, chartObject.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case ENE:
                setLocation(chartObject.getX() + chartObject.getWidth() - (getWidth() / 2) + intersectionFix, chartObject.getY() + (chartObject.getHeight() / 4) - (getWidth() / 2));
                break;
            case E:
                setLocation(chartObject.getX() + chartObject.getWidth() - (getWidth() / 2) + intersectionFix, chartObject.getY() + (chartObject.getHeight() / 2) - (getWidth() / 2));
                break;
            case ESE:
                setLocation(chartObject.getX() + chartObject.getWidth() - (getWidth() / 2) + intersectionFix, chartObject.getY() + (chartObject.getHeight() / 4) * 3 - (getWidth() / 2));
                break;
            case SE:
                setLocation(chartObject.getX() + chartObject.getWidth() - (getWidth() / 2) + intersectionFix, chartObject.getY() + chartObject.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SSE:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 4) * 3 - (getWidth() / 2), chartObject.getY() + chartObject.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case S:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 2) - (getWidth() / 2), chartObject.getY() + chartObject.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SSW:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 4) - (getWidth() / 2), chartObject.getY() + chartObject.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case SW:
                setLocation(chartObject.getX() - (getWidth() / 2), chartObject.getY() + chartObject.getHeight() - (getHeight() / 2) + intersectionFix);
                break;
            case WSW:
                setLocation(chartObject.getX() - (getWidth() / 2) - intersectionFix, chartObject.getY() + (chartObject.getHeight() / 4) * 3 - (getWidth() / 2));
                break;
            case W:
                setLocation(chartObject.getX() - (getWidth() / 2) - intersectionFix, chartObject.getY() + (chartObject.getHeight() / 2) - (getWidth() / 2));
                break;
            case WNW:
                setLocation(chartObject.getX() - (getWidth() / 2) - intersectionFix, chartObject.getY() + (chartObject.getHeight() / 4) - (getWidth() / 2));
                break;
            case NW:
                setLocation(chartObject.getX() - (getWidth() / 2) - intersectionFix, chartObject.getY() - (getHeight() / 2) - intersectionFix);
                break;
            case NNW:
                setLocation(chartObject.getX() + (chartObject.getWidth() / 4) - (getWidth() / 2), chartObject.getY() - (getHeight() / 2) - intersectionFix);
                break;
        }
    }

    public boolean isCorner() {
        return  position == Position.NE ||
                position == Position.NW ||
                position == Position.SE ||
                position == Position.SW;
    }

    // The offset methods are used by the FindPath class, to get a offset distance
    // and to calc if two connectors are attached to parallel sides or not.
    public static int[] getOffsets(Connector c) {
        return getOffsets(c, 50, 50);
    }

    public static int[] getOffsets(Connector c, int horizontal, int vertical) {
        switch (c.position) {
            case E:
            case ENE:
            case ESE:
                return new int[] {horizontal, 0};
            case N:
            case NNE:
            case NNW:
                return new int[] {0, -vertical};
            case S:
            case SSE:
            case SSW:
                return new int[] {0, vertical};
            case W:
            case WNW:
            case WSW:
                return new int[] {-horizontal, 0};
            case SE:
                return new int[] {horizontal, vertical};
            case SW:
                return new int[] {-horizontal, vertical};
            case NE:
                return new int[] {horizontal, -vertical};
            case NW:
                return new int[] {-horizontal, -vertical};
            default:
                return new int[] {0, 0};
        }
    }
}