package main.undoredo;

import java.awt.Rectangle;

/**
 * Interface to implement undo and redo
 */
public interface UndoRedo {
    void undo(UndoRedoNode node);
    void redo(UndoRedoNode node);
    Rectangle getBounds();
}