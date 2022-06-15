package listeners;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import main.undoredo.UndoRedoNode;
import staticmanagers.CommandManager;
import chartobjects.ChartObject;
import javax.swing.SwingUtilities;

/**
 * MouseMotionListener for ChartObject
 */
public class ChartObjectMouseMotion implements MouseMotionListener {
    private ChartObject chartObject;

    public ChartObjectMouseMotion(ChartObject chartObject) {
        this.chartObject = chartObject;
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isLeftMouseButton(e)) {

            // If dragging just started, add a undoable stack-frame containing all selected objects.
            if(!chartObject.getModel().getDragging()) {
                chartObject.getModel().addUndoableStackFrame(
                    chartObject.getModel().getSelectedObjects(),
                    UndoRedoNode.MOVED
                );
            }

            chartObject.getModel().setDragging(true);
            chartObject.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

            // Calc the change in location move
            int dx = e.getX() - chartObject.getStartPoint().x;
            int dy = e.getY() - chartObject.getStartPoint().y;

            // Check if y-lock och x-lock is active, otherwise move dx, dy
            chartObject.getModel().moveSelected(
                CommandManager.getCommand('y') ? 0 : dx,
                CommandManager.getCommand('x') ? 0 : dy
            );
        }
    }
}