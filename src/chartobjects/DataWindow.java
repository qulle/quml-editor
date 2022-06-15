package chartobjects;

import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
import main.Editor;
import staticmanagers.IconManager;

class DataWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    DataWindow() {
        super("Edit data", Editor.window.getGraphicsConfiguration());
        setResizable(false);
        setAlwaysOnTop(true);
        requestFocus();
        setIconImage(IconManager.getImage("32/logo"));
    }

    void updateWindow(ClassBox classBox) {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = 1;
        gbc.weighty = 0.0;

        contentPane.setLayout(gbl_contentPane);

        // BOX TYPE
        //--------------------------------------------------------------------------------------
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        contentPane.add(new JLabel("Type"), gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 1;

        final JComboBox<ClassBox.Type> cbBoxType = new JComboBox<>();
        cbBoxType.addActionListener(e -> {
            classBox.setType((ClassBox.Type)cbBoxType.getSelectedItem());
            classBox.repaint();
        });

        cbBoxType.setModel(new DefaultComboBoxModel<>(ClassBox.Type.values()));
        cbBoxType.setSelectedItem(classBox.getType());
        contentPane.add(cbBoxType, gbc);

        // TITLE
        //--------------------------------------------------------------------------------------
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 2;

        contentPane.add(new JLabel("Name"), gbc);

        final JTextField titleField = new JTextField(classBox.getName());
        titleField.setPreferredSize(new Dimension(0, 25));
        titleField.setMinimumSize(new Dimension(0, 25));
        titleField.setColumns(10);
        titleField.getDocument().addDocumentListener(new DocumentListener() {
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
                classBox.setName(titleField.getText());
                classBox.repaint();
            }
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 4;
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(titleField, gbc);

        final Checkbox cbTitleBold = new Checkbox("Final");
        cbTitleBold.setState(classBox.is(Data.FINAL));
        cbTitleBold.addItemListener(e -> {
            classBox.setNameAttribute(Data.FINAL, cbTitleBold.getState());
            classBox.repaint();
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = 3;
        contentPane.add(cbTitleBold, gbc);

        final Checkbox cbTitleUnderline = new Checkbox("Static");
        cbTitleUnderline.setState(classBox.is(Data.STATIC));
        cbTitleUnderline.addItemListener(e -> {
            classBox.setNameAttribute(Data.STATIC, cbTitleUnderline.getState());
            classBox.repaint();
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = 3;
        contentPane.add(cbTitleUnderline, gbc);

        final Checkbox cbTitleItalic = new Checkbox("Abstract");
        cbTitleItalic.setState(classBox.is(Data.ABSTRACT));
        cbTitleItalic.addItemListener(e -> {
            classBox.setNameAttribute(Data.ABSTRACT, cbTitleItalic.getState());
            classBox.repaint();
        });

        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 6;
        gbc.gridy = 3;
        contentPane.add(cbTitleItalic, gbc);

        // PROPERTIES
        //--------------------------------------------------------------------------------------
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 4;
        contentPane.add(new JLabel("Properties"), gbc);

        final JTextField txtNewProperty = new JTextField();
        txtNewProperty.setPreferredSize(new Dimension(0, 25));
        txtNewProperty.setMinimumSize(new Dimension(0, 25));
        txtNewProperty.setColumns(10);
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        contentPane.add(txtNewProperty, gbc);

        final Checkbox cbNewFinalProperty = new Checkbox("Final");
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = 5;
        contentPane.add(cbNewFinalProperty, gbc);

        final Checkbox cbNewStaticPropertie = new Checkbox("Static");
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = 5;
        contentPane.add(cbNewStaticPropertie, gbc);

        JButton btnAddProperty = new JButton("Add");
        btnAddProperty.addActionListener(e -> {
            classBox.addPropertyData(txtNewProperty.getText(), cbNewStaticPropertie.getState(), cbNewFinalProperty.getState());
            classBox.repaint();
            updateWindow(classBox);
        });

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 6;
        gbc.gridy = 5;
        contentPane.add(btnAddProperty, gbc);

        int y = 6;
        for(final Data d : classBox.getPropertiesData()) {
            final JTextField tf = new JTextField(d.getData());
            tf.setPreferredSize(new Dimension(0, 25));
            tf.setMinimumSize(new Dimension(0, 25));
            tf.setColumns(10);
            tf.getDocument().addDocumentListener(new DocumentListener() {
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
                    d.setData(tf.getText());
                    classBox.repaint();
                }
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = y;
            contentPane.add(tf, gbc);

            final Checkbox cbPropertyFinal = new Checkbox("Final");
            cbPropertyFinal.setState(d.is(Data.FINAL));
            cbPropertyFinal.addItemListener(e -> {
                d.setAttribute(Data.FINAL, cbPropertyFinal.getState());
                classBox.repaint();
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 4;
            gbc.gridy = y;
            contentPane.add(cbPropertyFinal, gbc);

            final Checkbox cbPropertyStatic = new Checkbox("Static");
            cbPropertyStatic.setState(d.is(Data.STATIC));
            cbPropertyStatic.addItemListener(e -> {
                d.setAttribute(Data.STATIC, cbPropertyStatic.getState());
                classBox.repaint();
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 5;
            gbc.gridy = y;
            contentPane.add(cbPropertyStatic, gbc);

            JButton btnDeleteProperty = new JButton("Delete");
            btnDeleteProperty.addActionListener(e -> {
                classBox.deletePropertyData(d);
                classBox.repaint();
                updateWindow(classBox);
            });

            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 6;
            gbc.gridy = y;
            contentPane.add(btnDeleteProperty, gbc);
            y++;
        }

        // METHODS
        //--------------------------------------------------------------------------------------
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = y;
        contentPane.add(new JLabel("Methods"), gbc);

        y++;

        final JTextField txtNewMethod = new JTextField();
        txtNewMethod.setPreferredSize(new Dimension(0, 25));
        txtNewMethod.setMinimumSize(new Dimension(0, 25));
        txtNewMethod.setColumns(10);
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = y;
        contentPane.add(txtNewMethod, gbc);

        final Checkbox cbNewFinalMethod = new Checkbox("Final");
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 3;
        gbc.gridy = y;
        contentPane.add(cbNewFinalMethod, gbc);

        final Checkbox cbNewStaticMethod = new Checkbox("Static");
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 4;
        gbc.gridy = y;
        contentPane.add(cbNewStaticMethod, gbc);

        final Checkbox cbNewAbstractMethod = new Checkbox("Abstract");
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 5;
        gbc.gridy = y;
        contentPane.add(cbNewAbstractMethod, gbc);

        JButton btnNewMethod = new JButton("Add");
        btnNewMethod.addActionListener(e -> {
            classBox.addMethodData(txtNewMethod.getText(), cbNewAbstractMethod.getState(), cbNewStaticMethod.getState(), cbNewFinalMethod.getState());
            classBox.repaint();
            updateWindow(classBox);
        });

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 6;
        gbc.gridy = y;
        contentPane.add(btnNewMethod, gbc);

        y++;

        for(final Data d : classBox.getMethodsData()) {
            final JTextField tf = new JTextField(d.getData());
            tf.setPreferredSize(new Dimension(0, 25));
            tf.setMinimumSize(new Dimension(0, 25));
            tf.setColumns(10);
            tf.getDocument().addDocumentListener(new DocumentListener() {
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
                    d.setData(tf.getText());
                    classBox.repaint();
                }
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = y;
            contentPane.add(tf, gbc);

            final Checkbox cbFinalMethod = new Checkbox("Final");
            cbFinalMethod.setState(d.is(Data.FINAL));
            cbFinalMethod.addItemListener(e -> {
                d.setAttribute(Data.FINAL, cbFinalMethod.getState());
                classBox.repaint();
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 3;
            gbc.gridy = y;
            contentPane.add(cbFinalMethod, gbc);

            final Checkbox cbStaticMethod = new Checkbox("Static");
            cbStaticMethod.setState(d.is(Data.STATIC));
            cbStaticMethod.addItemListener(e -> {
                d.setAttribute(Data.STATIC, cbStaticMethod.getState());
                classBox.repaint();
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 4;
            gbc.gridy = y;
            contentPane.add(cbStaticMethod, gbc);

            final Checkbox cbAbstractMethod = new Checkbox("Abstract");
            cbAbstractMethod.setState(d.is(Data.ABSTRACT));
            cbAbstractMethod.addItemListener(e -> {
                d.setAttribute(Data.ABSTRACT, cbAbstractMethod.getState());
                classBox.repaint();
            });

            gbc.insets = new Insets(0, 0, 5, 5);
            gbc.weightx = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 5;
            gbc.gridy = y;
            contentPane.add(cbAbstractMethod, gbc);

            JButton btnDeleteMethod = new JButton("Delete");
            btnDeleteMethod.addActionListener(e -> {
                classBox.deleteMethodData(d);
                classBox.repaint();
                updateWindow(classBox);
            });
            
            gbc.weightx = 0.0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 6;
            gbc.gridy = y;
            contentPane.add(btnDeleteMethod, gbc);
            y++;
        }

        // Add empty element last to fix layout
        gbc.weighty = Double.MIN_VALUE;
        gbc.gridx = 0;
        gbc.gridy = y;
        contentPane.add(new JLabel(), gbc);

        setSize(550, (y - 4) * 30 + 130);
        setContentPane(contentPane);
        revalidate();
    }
}