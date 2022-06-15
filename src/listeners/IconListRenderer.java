package listeners;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import lines.ChartLine;
import relations.ConnectorSymbol;

/**
 * Adds a icon to the items in a Component ex. JComboBox
 */
public class IconListRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Get the renderer component from parent class
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        // Get icon to use for the list item value
        ImageIcon icon = new ImageIcon();

        // TODO: Try to make this better, no huge if-block when adding more stuff
        if(value instanceof ConnectorSymbol) {
            icon = ((ConnectorSymbol)value).getIcon();
        }else if(value instanceof ChartLine.Type) {
            icon = ((ChartLine.Type)value).getIcon();
        }

        // Set icon to display for value
        label.setIcon(icon);
        return label;
    }
}