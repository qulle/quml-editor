package chartobjects;

import java.io.Serializable;

/**
 * Represents the UML-box data
 */
public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    static final int ABSTRACT = 0;
    static final int STATIC   = 1;
    static final int FINAL    = 2;

    private String data;
    private boolean[] attributes = new boolean[] {
        false, // Italic Abstract
        false, // Underline Static
        false, // Bold final
    };

    Data(String data, boolean italic, boolean underline, boolean bold) {
        this.data = data;
        attributes[ABSTRACT] = italic;
        attributes[STATIC] = underline;
        attributes[FINAL] = bold;
    }

    public Data copy() {
        return new Data(
            data,
            attributes[ABSTRACT],
            attributes[STATIC],
            attributes[FINAL]
        );
    }

    void setAttribute(int index, boolean value) {
        attributes[index] = value;
    }

    public boolean is(int index) {
        return attributes[index];
    }

    void setData(String data) {
        this.data = data;
    }

    String getData() {
        return data;
    }
}