package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import staticmanagers.CommandManager;
import main.DrawingSurface;

public class DrawingSurfaceKey implements KeyListener {
    private DrawingSurface drawingSurface;

    public DrawingSurfaceKey(DrawingSurface drawingSurface) {
        this.drawingSurface = drawingSurface;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        CommandManager.removeCommand(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        drawingSurface.doCommand(e);
    }
}