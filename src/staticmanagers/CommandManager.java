package staticmanagers;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * CommandManager handles commands that needs to be stored a longer time
 * 'x' - locks drag-move to x-axis
 * 'y' - locks drag-move to y-axis
 * ' ' - when drawingSurface is dragged (space)
 */

public final class CommandManager {
    private CommandManager() {}

    private static Set<Character> commands = new HashSet<>();

    public static void setCommand(KeyEvent e) {
        commands.add(e.getKeyChar());
    }

    public static void removeCommand(KeyEvent e) {
        commands.remove(e.getKeyChar());
    }

    public static boolean getCommand(int keyCode) {
        return getCommand((char)keyCode);
    }

    public static boolean getCommand(char c) {
        return commands.contains(c);
    }
}