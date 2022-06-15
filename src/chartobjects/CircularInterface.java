package chartobjects;

import main.Editor;
import main.Model;
import chartobjects.Connector.Position;
import staticmanagers.ColorManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;

public class CircularInterface extends OOPObject {
    private JLabel nameLabel = new JLabel("Serializable");

    public CircularInterface(Model model, Point clickedPoint) {
        super(model, Type.INTERFACE);
        setBackground(ColorManager.INTERFACE_BACKGROUND.getColor());

        final int initWidth = 70;
        final int initHeight = 70;

        setBounds(clickedPoint.x - (initWidth / 2), clickedPoint.y - (initHeight / 2), initWidth, initHeight);
        setOpaque(false);
        addConnectors();
        addListeners();
    }

    private CircularInterface(Model model, Rectangle bounds, JLabel nameLabel) {
        this(model, bounds.getLocation());

        this.nameLabel = new JLabel(nameLabel.getText());

        setBounds(bounds);
        setConnectorLocation();
    }

    @Override
    public void addConnectors() {
        addConnector(new Connector(this, Position.N));
        addConnector(new Connector(this, Position.S));
        addConnector(new Connector(this, Position.E));
        addConnector(new Connector(this, Position.W));
    }

    @Override
    public CircularInterface copy() {
        return new CircularInterface(
                getModel(),
                getBounds(),
                nameLabel
        );
    }

    @Override
    public void doCommand(KeyEvent e) {
        getModel().getDrawingSurface().doCommand(e);
    }

    @Override
    public void openEditWindow() {
        String value = JOptionPane.showInputDialog(Editor.window, "Enter name");
        if(value != null && !value.isEmpty()) {
            nameLabel.setText(value);
            setLabelLocation();
        }
    }

    @Override
    public void addInternalComponents(){
        super.addInternalComponents();
        getModel().getDrawingSurface().add(nameLabel, 0);
        setLabelLocation();
    }

    @Override
    public void removeInternalComponents() {
        super.removeInternalComponents();
        getModel().getDrawingSurface().remove(nameLabel);
    }

    @Override
    public void dragMove(int dx, int dy) {
        super.dragMove(dx, dy);
        setLabelLocation();
    }

    @Override
    public void setConnectorLocation() {
        super.setConnectorLocation();
        setLabelLocation();
    }

    private void setLabelLocation() {
        nameLabel.setSize(nameLabel.getPreferredSize());
        final int yOffset = 20;
        int xOffset = getWidth() - nameLabel.getWidth();

        nameLabel.setLocation(getX() + (xOffset / 2), getY() - yOffset);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        final int borderWidth = 2;
        g2.setColor(getBackground());
        g2.fillOval(1, 1, getWidth() - borderWidth, getHeight() - borderWidth);
        g2.setStroke(new BasicStroke(borderWidth));
        g2.setColor(ColorManager.BOX_BORDER.getColor());
        g2.drawOval(1, 1, getWidth() - borderWidth, getHeight() - borderWidth);
    }
}