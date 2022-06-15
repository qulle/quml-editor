package main.undoredo;

import java.awt.Rectangle;

public class UndoRedoNode {
    public static final int ADDED   = 0;
    public static final int DELETED = 1;
    public static final int MOVED   = 2;
    public static final int REZISED = 3;

    private UndoRedo item;
    private Rectangle bounds;
    private boolean[] events = new boolean[] {
        false, // Added
        false, // Deleted
        false, // Moved
        false, // Resized
    };

    public UndoRedoNode(UndoRedo item, Rectangle bounds, int... indexes) {
        this.item = item;
        this.bounds = bounds;

        for(int i : indexes) {
            events[i] = true;
        }
    }

    public UndoRedoNode(UndoRedo item, Rectangle bounds, boolean[] events) {
        this.item = item;
        this.bounds = bounds;
        this.events = events;
    }

    public UndoRedo getItem() {
        return item;
    }

    public boolean isSet(int index) {
        return events[index];
    }

    public boolean[] getAndToggleEvents() {
        boolean[] toggled = events.clone();

        toggled[ADDED]   = !toggled[ADDED];
        toggled[DELETED] = !toggled[DELETED];

        return toggled;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}