package relations;

import java.awt.Graphics2D;
import java.io.Serializable;
import javax.swing.ImageIcon;
import lines.ChartLine;

/**
 * The abstract superclass for line-connector-symbols
 */
public abstract class ConnectorSymbol implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int NONE = 0;
    public static final int ASSOCIATION = 1;
    public static final int INHERITANCE = 2;
    public static final int IMPLEMENTATION = 3;
    public static final int DEPENDENCY = 4;
    public static final int AGGREGATION = 5;
    public static final int COMPOSITION = 6;

    public abstract void draw(Graphics2D g2, ChartLine chartLine);
    public abstract int getType();
    public abstract ImageIcon getIcon();
    public abstract String toString();

    private final int height = 18;
    private final int width = 10;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean is(int value) {
        return getType() == value;
    }
}