package lines;

import java.awt.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import listeners.IconListRenderer;
import relations.ConnectorSymbol;
import staticmanagers.IconManager;
import staticmanagers.WindowManager;

class LineWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    LineWindow() {
        super("Edit line");
        setResizable(false);
        setAlwaysOnTop(true);
        requestFocus();
        setSize(425, 190);
        setIconImage(IconManager.getImage("32/logo"));
        WindowManager.centerWindow(this);
    }

    void updateWindow(ChartLine chartLine) {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagLayout gblContentPane = new GridBagLayout();
        gblContentPane.columnWidths = new int[]{0, 0};
        gblContentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
        gblContentPane.columnWeights = new double[]{1.0, 1.0};
        gblContentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        contentPane.setLayout(gblContentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(new JLabel("Linetype"), gbc);

        final JComboBox<ChartLine.Type> cbLineType = new JComboBox<>();
        cbLineType.setRenderer(new IconListRenderer());
        cbLineType.addActionListener(e -> {
            chartLine.setLineType((ChartLine.Type)cbLineType.getSelectedItem());
            chartLine.getModel().update();
        });

        cbLineType.setModel(new DefaultComboBoxModel<>(ChartLine.Type.values()));
        cbLineType.setSelectedItem(chartLine.getLineType());

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(cbLineType, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(new JLabel("Relation"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(new JLabel("Flipp line"), gbc);

        final JComboBox<ConnectorSymbol> cbStart = new JComboBox<>(chartLine.getModel().getConnectorSymbols());
        cbStart.setRenderer(new IconListRenderer());
        cbStart.addActionListener(e -> {
            chartLine.setStartConnectorSymbol(chartLine.getModel().getConnectorSymbol((ConnectorSymbol)cbStart.getSelectedItem()));
            chartLine.getModel().getDrawingSurface().repaint();
        });

        cbStart.setSelectedItem(chartLine.getStartConnectorSymbol());

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(cbStart, gbc);

        JButton flipRelation = new JButton("Flipp");
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(flipRelation, gbc);

        flipRelation.addActionListener(e -> {
            chartLine.swap();
            chartLine.getModel().update();
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(new JLabel("Labeltext"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPane.add(new JLabel("Labeltext"), gbc);

        final JTextField tfStartLabel = new JTextField(chartLine.getLineLabel(0).getText());
        tfStartLabel.setPreferredSize(new Dimension(0, 25));
        tfStartLabel.setMinimumSize(new Dimension(0, 25));
        tfStartLabel.setColumns(10);
        tfStartLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateData();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateData();
            }

            private void updateData() {
                chartLine.setLineLabel(0, tfStartLabel.getText());
            }
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        contentPane.add(tfStartLabel, gbc);

        final JTextField tfEndLabel = new JTextField(chartLine.getLineLabel(1).getText());
        tfEndLabel.setPreferredSize(new Dimension(0, 25));
        tfEndLabel.setMinimumSize(new Dimension(0, 25));
        tfEndLabel.setColumns(10);
        tfEndLabel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateData();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateData();
            }

            private void updateData() {
                chartLine.setLineLabel(1, tfEndLabel.getText());
            }
        });
        
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 5;
        contentPane.add(tfEndLabel, gbc);

        setContentPane(contentPane);
    }
}