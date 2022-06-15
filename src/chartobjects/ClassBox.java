package chartobjects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import staticmanagers.ColorManager;
import main.Model;

public class ClassBox extends OOPObject {
    private static final long serialVersionUID = 1L;
    private static DataWindow dataWindow = new DataWindow();

    private List<Data> propertiesData = new ArrayList<>();
    private List<Data> methodsData = new ArrayList<>();
    private Data nameData = new Data("Name", false, false, false);

    public ClassBox(Model model, Point clickedPoint) {
        super(model, Type.CLASS);
        setBackground(ColorManager.CLASS_BACKGROUND.getColor());
        setBorder(new LineBorder(ColorManager.BOX_BORDER.getColor(), 2));

        final int initWidth = 220;
        final int initHeight = 200;
        setBounds(clickedPoint.x - (initWidth / 2), clickedPoint.y - (initHeight / 2), initWidth, initHeight);

        addConnectors();
        addListeners();
    }

    private ClassBox(Model model, Rectangle bounds, Data nameData, List<Data> propertiesData, List<Data> methodsData) {
        this(model, bounds.getLocation());

        this.nameData = nameData;
        this.propertiesData = new ArrayList<>(propertiesData);
        this.methodsData = new ArrayList<>(methodsData);

        setBounds(bounds);
        setConnectorLocation();
    }

    @Override
    public ClassBox copy() {
        return new ClassBox(
            getModel(),
            getBounds(),
            nameData.copy(),
            getPropertiesData(),
            getMethodsData()
        );
    }

    @Override
    public void doCommand(KeyEvent e) {
        getModel().getDrawingSurface().doCommand(e);
    }

    @Override
    public void openEditWindow() {
        dataWindow.updateWindow(this);
        dataWindow.setVisible(true);
    }

    @Override
    public void setName(String value) {
        nameData.setData(value);
    }

    @Override
    public String getName() {
        return nameData.getData();
    }

    void setNameAttribute(int index, boolean value) {
        nameData.setAttribute(index, value);
    }

    public boolean is(int index) {
        return nameData.is(index);
    }

    List<Data> getPropertiesData() {
        return propertiesData;
    }

    List<Data> getMethodsData() {
        return methodsData;
    }

    void addPropertyData(String item, boolean underline, boolean bold) {
        propertiesData.add(new Data(item, false, underline, bold));
    }

    void deletePropertyData(Data item) {
        propertiesData.remove(item);
    }

    void addMethodData(String item, boolean italic, boolean underline, boolean bold) {
        methodsData.add(new Data(item, italic, underline, bold));
    }

    void deleteMethodData(Data item) {
        methodsData.remove(item);
    }

    private void setFont(Graphics g, Data d) {
        if(d.is(Data.FINAL) && d.is(Data.ABSTRACT)) {
            g.setFont(g.getFont().deriveFont((Font.BOLD + Font.ITALIC), 14));
        }else if(d.is(Data.FINAL)) {
            g.setFont(g.getFont().deriveFont((Font.BOLD), 14));
        }else if(d.is(Data.ABSTRACT)) {
            g.setFont(g.getFont().deriveFont((Font.ITALIC), 14));
        }else {
            g.setFont(g.getFont().deriveFont((Font.PLAIN), 14));
        }
    }

    private int printBoxData(Graphics g , List<Data> data, int x, int y) {
        for(Data d : data) {
            setFont(g, d);
            g.drawString(d.getData(), 15, y + 30);
            if(d.is(Data.STATIC)) {
                g.drawLine(x, y + 35, x + g.getFontMetrics().stringWidth(d.getData()), y + 35);
            }
            y += 25;
        }
        return y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int offsetY = 50;
        int offsetX;

        g.setColor(ColorManager.BOX_BORDER.getColor());
        g.drawLine(0, offsetY, getWidth(), offsetY);

        // BOX TYPE
        offsetY = 20;
        g.setColor(Color.BLACK);
        g.drawString("<<" + getType() + ">>", (getWidth() / 2) - (g.getFontMetrics().stringWidth("<<" + getType() + ">>") / 2), offsetY);

        // TITLE
        setFont(g, nameData);
        offsetX = (getWidth() / 2) - (g.getFontMetrics().stringWidth(getName()) / 2);
        offsetY = 40;
        g.drawString(getName(), offsetX, offsetY);

        offsetY = 45;
        if(is(Data.STATIC)){
            g.drawLine(offsetX, offsetY, offsetX + g.getFontMetrics().stringWidth(getName()), offsetY);
        }

        // PROPERTIES
        offsetX = 15;
        offsetY = printBoxData(g, propertiesData, offsetX, offsetY);

        g.setColor(ColorManager.BOX_BORDER.getColor());
        g.drawLine(0, (offsetY < 50 ? 50 : offsetY + 20), getWidth(), (offsetY < 50 ? 50 : offsetY + 20));
        offsetY += 10;

        // METHODS
        g.setColor(Color.BLACK);
        printBoxData(g, methodsData, offsetX, offsetY);

        // WHEN RESIZING
        if(isResizing()) {
            g.setColor(ColorManager.BOX_RESIZING_BACKGROUND.getColor());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.setFont(g.getFont().deriveFont((Font.BOLD), 14));
            String dim = getWidth() + " * " + getHeight();
            g.drawString(dim, (getWidth() / 2) - (g.getFontMetrics().stringWidth(dim) / 2), getHeight() / 2 + 20);
        }
    }
}