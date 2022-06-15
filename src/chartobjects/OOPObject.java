package chartobjects;

import main.Model;
import staticmanagers.ColorManager;
import java.awt.Color;

public abstract class OOPObject extends ChartObject {
    public enum Type {
        INTERFACE(ColorManager.INTERFACE_BACKGROUND.getColor()),
        CLASS(ColorManager.CLASS_BACKGROUND.getColor()),
        ENUM(ColorManager.ENUM_BACKGROUND.getColor());

        private Color color;

        Type(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    private Type type;

    OOPObject(Model model, Type type) {
        super(model);
        this.type = type;
    }

    @Override
    public Color getColor() {
        return type.getColor();
    }

    public Type getType() {
        return type;
    }

    void setType(Type type) {
        this.type = type;
        setBackground(type.getColor());
    }
}