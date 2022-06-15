package relations;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.ImageIcon;
import staticmanagers.ColorManager;
import staticmanagers.IconManager;
import lines.ChartLine;

public class Aggregation extends ConnectorSymbol {
    private static final long serialVersionUID = 1L;

    @Override
    public int getType() {
        return ConnectorSymbol.AGGREGATION;
    }

    @Override
    public ImageIcon getIcon() {
        return IconManager.getIcon("16/aggregation");
    }

    @Override
    public String toString() {
        return "Aggregation";
    }

    @Override
    public void draw(Graphics2D g2, ChartLine chartLine) {
        g2.setStroke(new BasicStroke(2));

        Line2D[] path = chartLine.getPath();

        // The line that gets the symbol
        Line2D line = path[0];

        double x1 = line.getX1();
        double y1 = line.getY1();
        double x2 = line.getX2();
        double y2 = line.getY2();

        double dx = x2 - x1;
        double dy = y2 - y1;

        double sqrt = Math.sqrt(dx * dx + dy * dy);

        double xm = getHeight();
        double xn = xm;
        double xo = xm * 2;
        double ym = getWidth();
        double yn = -getWidth();
        double yo = 0;
        double x;

        double sin = dy / sqrt;
        double cos = dx / sqrt;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        x = xo * cos - yo * sin + x1;
        yo = xo * sin + yo * cos + y1;
        xo = x;

        int[] xPoints = new int[] {(int) x1, (int) xm, (int) xo, (int) xn};
        int[] yPoints = new int[] {(int) y1, (int) ym, (int) yo, (int) yn};

        g2.setColor(ColorManager.DRAWING_SURFACE_BACKGROUND.getColor());
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.setColor(ColorManager.LINE.getColor());
        g2.drawPolygon(xPoints, yPoints, 4);
    }
}