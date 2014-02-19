package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.shared.Comment;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentSection extends Composite {

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public CommentSection(ArrayList<Comment> comments) {
        VerticalPanel p = new VerticalPanel();


        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("comment-section");
    }

    public CommentSection(int threadId) {

    }
}