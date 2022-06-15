package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import staticmanagers.CommandManager;
import chartobjects.ChartObject;

public class ChartObjectKey implements KeyListener {
    private ChartObject chartObject;

    public ChartObjectKey(ChartObject chartObject) {
        this.chartObject = chartObject;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        chartObject.doCommand(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandManager.removeCommand(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}