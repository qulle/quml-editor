package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import staticmanagers.CommandManager;
import chartobjects.Connector;

public class ConnectorKey implements KeyListener {
    private Connector connector;

    public ConnectorKey(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        connector.getChartObject().doCommand(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        CommandManager.removeCommand(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}