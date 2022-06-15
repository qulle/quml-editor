package main;

import menus.SideBar;
import javax.swing.JTabbedPane;
import java.util.ArrayList;
import java.util.List;

public class TabManager extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private Editor editor;
    private List<WorkSpace> workSpaces = new ArrayList<>();

    TabManager(Editor editor) {
        this.editor = editor;
        setFocusable(false);
        addTab();

        // Update the zoom Label when selected tab is changed, since the new tab contains a different model
        addChangeListener(e -> {
            if(getTabCount() > 0) {
                editor.getSideBar().updateZoomLabel();
            }
        });
    }

    public void addTab() {
        int index = getTabCount();
        WorkSpace ws = new WorkSpace(this);

        workSpaces.add(index, ws);
        addTab("Drawing " + getTabCount(), ws);

        // Select the created tab and set focus to drawing-surface, so that key-commands will trigger
        setSelectedIndex(index);
        ws.getDrawingSurface().requestFocus();
    }

    public void removeTab() {
        int index = getSelectedIndex();

        workSpaces.remove(index);
        removeTabAt(index);

        // If last tab was removed, add a new tab
        if(getTabCount() == 0) {
            addTab();
        }

        // Set focus to drawing-surface, so that key-commands will trigger
        index = getSelectedIndex();
        workSpaces.get(index).getDrawingSurface().requestFocus();
    }

    public Model getCurrentModel() {
        return workSpaces.get(getSelectedIndex()).getDrawingSurface().getModel();
    }

    public WorkSpace getCurrentWorkSpace() {
        return workSpaces.get(getSelectedIndex());
    }

    public SideBar getSideBar() {
        return editor.getSideBar();
    }

    void updateTabLabel(String text) {
        setTitleAt(getSelectedIndex(), text);
    }
}