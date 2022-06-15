package staticmanagers;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * IconManager loads specified icons to a collection
 * Access icons using IconManager.getIcon("size/icon name")
 */
public final class IconManager {
    private IconManager() {}
    private static final Map<String, ImageIcon> IMAGEICONS = new HashMap<>();
    private static boolean hasLoaded = false;

    private static final String[] ICONSTOLOAD = new String[] {
        // Contextmenus
        "16/trash_bin.png",
        "16/new_document.png",
        "16/close_document.png",
        "16/edit.png",
        "16/save.png",
        "16/export.png",
        "16/open.png",
        "16/grid.png",
        "16/class.png",
        "16/interface.png",
        "16/copy.png",
        "16/paste.png",
        // Relations
        "16/aggregation.png",
        "16/association.png",
        "16/composition.png",
        "16/dependency.png",
        "16/implementing.png",
        "16/inheritance.png",
        "16/none.png",
        // Line types
        "16/bent.png",
        "16/semi.png",
        "16/straight.png",
        // SideBar
        "32/export.png",
        "32/save.png",
        "32/trash_bin.png",
        "32/zoom_in.png",
        "32/zoom_out.png",
        "32/new_document.png",
        "32/close_document.png",
        "32/open.png",
        "32/undo.png",
        "32/redo.png",
        "32/copy.png",
        "32/paste.png",
        // Misc
        "32/logo.png",
        "start-screen.png"
    };

    public static void loadImages() {
        if(!hasLoaded) {
            URL filePath;
            for(String name : ICONSTOLOAD) {
                filePath = ClassLoader.getSystemClassLoader().getResource("icons/" + name);
                if(filePath != null) {
                    IMAGEICONS.put(name.split("\\.")[0], new ImageIcon(filePath));
                }
            }
            
            hasLoaded = true;
        }
    }

    public static Image getImage(String name) {
        return getIcon(name).getImage();
    }

    public static ImageIcon getIcon(String name) {
        return IMAGEICONS.get(name);
    }
}