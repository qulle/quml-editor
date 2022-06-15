package listeners;

import chartobjects.Comment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CommentFocus implements FocusListener {
    private Comment comment;

    public CommentFocus(Comment comment) {
        this.comment = comment;
    }

    @Override
    public void focusGained(FocusEvent e) {
        comment.getTextArea().setOpaque(true);
        comment.getTextArea().setEnabled(true);
    }

    @Override
    public void focusLost(FocusEvent e) {
        comment.getTextArea().setOpaque(false);
        comment.getTextArea().setEnabled(false);
    }
}