package staticmanagers;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.Toolkit;

public final class WindowManager {
    private WindowManager() {}

    // Center a JFrame, WindowManager.centerWindow(theFrame)
    public static void centerWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth()  - frame.getWidth())  / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);

        frame.setLocation(x, y);
    }

    // TODO: Add functionality to help with dual monitors,  ex. open program on same monitor it was closed
}
