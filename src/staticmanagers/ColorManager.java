package staticmanagers;

import java.awt.Color;

/**
 * ColorManager handles all the Colors in the application
 * Use: ColorManager.theColor.getColor()
 */
public enum ColorManager {
    /** #FFFFFF */
    DRAWING_SURFACE_BACKGROUND("#FFFFFF"),
    /** #F0F4F5 */
    CLASS_BACKGROUND("#F0F4F5"),
    /** #EFDCDF */
    INTERFACE_BACKGROUND("#EFDCDF"),
    /** #D7EDDD */
    ENUM_BACKGROUND("#D7EDDD"),
    /** #E3F0F7 */
    COMMENT_BACKGROUND("#E3F0F7"),
    /** #FFE6B2 */
    BOX_SELECTED_BACKGROUND("#FFE6B2"),
    /** r255 g230 b178 a200 */
    BOX_RESIZING_BACKGROUND(new Color(255, 230, 178, 200)),
    /** #5D6063 */
    BOX_BORDER("#5D6063"),
    /** #B2DFDB */
    CONNECTOR_BACKGROUND("#B2DFDB"),
    /** #FFE6B2 */
    CONNECTOR_HOVER_BACKGROUND("#FFE6B2"),
    /** #07977D */
    CONNECTOR_BORDER("#07977D"),
    /** #EEEEEE */
    SIDEBAR_BACKGROUND("#EEEEEE"),
    /** #FFA45E */
    ORANGE_LINE("#FFA45E"),
    /** #609EC7 */
    LINE("#609EC7"),
    /** r213 g235 b242 a127 */
    SELECTION_BACKGROUND(new Color(213, 235, 242, 127)),
    /** #74A5C1 */
    SELECTION_BORDER("#74A5C1"),
    /** #EEEEEE */
    GRID("#EEEEEE");

    private final Color color;

    ColorManager(String hex) {
        this(Color.decode(hex));
    }

    ColorManager(Color color) {
        this.color = color;
    }

    public final Color getColor() {
        return color;
    }
}