package relations;

import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import staticmanagers.IconManager;
import lines.ChartLine;

public class None extends ConnectorSymbol {
    private static final long serialVersionUID = 1L;

    @Override
    public ImageIcon getIcon() {
        return IconManager.getIcon("16/none");
    }

    @Override
    public int getType() {
        return ConnectorSymbol.NONE;
    }

    @Override
    public String toString() {
        return "None";
    }

    @Override
    public void draw(Graphics2D g2, ChartLine chartLine) {}
}