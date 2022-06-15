package chartobjects;

import listeners.ChartObjectMouse;
import listeners.ChartObjectMouseMotion;
import listeners.CommentFocus;
import main.Model;
import staticmanagers.ColorManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Comment extends ChartObject {
    private JTextArea jta;

    public Comment(Model model, Point clickedPoint) {
        super(model);
        setBorder(new LineBorder(ColorManager.BOX_BORDER.getColor(), 2));
        setBackground(ColorManager.COMMENT_BACKGROUND.getColor());

        final int initWidth = 220;
        final int initHeight = 150;

        setBounds(clickedPoint.x - (initWidth / 2), clickedPoint.y - (initHeight / 2), initWidth, initHeight);
        setLayout(new BorderLayout());

        addConnectors();

        jta = new JTextArea("Comment");
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setBackground(Color.WHITE);
        jta.setOpaque(false);
        jta.setEnabled(false);
        jta.setDisabledTextColor(Color.BLACK);

        JPanel innerContent = new JPanel();
        innerContent.setLayout(new BorderLayout());
        final int padding = 10;
        innerContent.setBorder(new EmptyBorder(padding, padding, padding, padding));
        innerContent.setOpaque(false);
        innerContent.add(jta);

        add(innerContent);

        addListeners();
    }

    private Comment(Model model, Rectangle bounds, String text) {
        this(model, bounds.getLocation());
        jta.setText(text);
    }

    @Override
    public void addListeners() {
        super.addListeners();

        jta.addFocusListener(new CommentFocus(this));
        jta.addMouseListener(new ChartObjectMouse(this));
        jta.addMouseMotionListener(new ChartObjectMouseMotion(this));
    }

    @Override
    public Comment copy() {
        return new Comment(
            getModel(),
            getBounds(),
            jta.getText()
        );
    }

    @Override
    public void doCommand(KeyEvent e) {
        getModel().getDrawingSurface().doCommand(e);
    }

    @Override
    public void openEditWindow() {
        jta.requestFocus();
    }

    @Override
    public Color getColor() {
        return ColorManager.COMMENT_BACKGROUND.getColor();
    }

    public JTextArea getTextArea() {
        return jta;
    }
}