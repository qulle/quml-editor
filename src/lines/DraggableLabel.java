package lines;

import java.awt.*;
import javax.swing.JLabel;
import chartobjects.Connector;
import listeners.DraggableLabelMouse;
import listeners.DraggableLabelMouseMotion;
import staticmanagers.ColorManager;

/**
 * DraggableLabel is the labels connected to each chartLine
 */
public class DraggableLabel extends JLabel {
    private static final long serialVersionUID = 1L;
    private ChartLine chartLine;
    private Point clickedPoint = null;
    private Connector anchorPoint = null;
    private int xOffset;
    private int yOffset;
    private boolean isDragging = false;

    DraggableLabel(ChartLine chartLine) {
        this(chartLine, "", 0, 0);
    }

    private DraggableLabel(ChartLine chartLine, String text, int xOffset, int yOffset) {
        super(text);

        this.chartLine = chartLine;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        setBackground(ColorManager.DRAWING_SURFACE_BACKGROUND.getColor());
        setOpaque(true);
        addListeners();
    }

    public DraggableLabel copy() {
        return new DraggableLabel(
            chartLine,
            getText(),
            xOffset,
            yOffset
        );
    }

    void addListeners() {
        addMouseListener(new DraggableLabelMouse(this));
        addMouseMotionListener(new DraggableLabelMouseMotion(this));
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        setSize(getPreferredSize());
    }

    public ChartLine getChartLine() {
        return chartLine;
    }

    public void setClickedPoint(Point clickedPoint) {
        this.clickedPoint = clickedPoint;
    }

    public Point getClickedPoint() {
        return clickedPoint;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void setDragging(boolean flag) {
        isDragging = flag;
    }

    void setAnchorPoint(Connector anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public int[] getAnchorPointCoordinates() {
        int x = anchorPoint.getX() + Connector.getOffsets(anchorPoint)[0];
        int y = anchorPoint.getY() + Connector.getOffsets(anchorPoint)[1];

        return new int[]{x, y};
    }

    void updateLocation() {
        int[] anchorCoordinates = getAnchorPointCoordinates();
        setLocation(anchorCoordinates[0] - xOffset, anchorCoordinates[1] - yOffset);
    }

    void render(Graphics2D g2) {
        if(isDragging) {
            g2.setColor(ColorManager.ORANGE_LINE.getColor());
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{3}, 0));
            g2.drawLine(
                getX() + getWidth()  / 2,
                getY() + getHeight() / 2,
                anchorPoint.getX() + anchorPoint.getWidth()  / 2,
                anchorPoint.getY() + anchorPoint.getHeight() / 2
            );
        }
    }
}