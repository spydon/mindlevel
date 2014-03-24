package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.CommentService;
import net.mindlevel.client.services.CommentServiceAsync;
import net.mindlevel.shared.Comment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentSection extends Composite {

    private final ArrayList<Comment> comments;
    private final VerticalPanel p;
    public final static int MAX_COMMENT_DEPTH = 3;

    private final CommentServiceAsync commentService = GWT
            .create(CommentService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public CommentSection(final int threadId) {
        p = new VerticalPanel();
        comments = new ArrayList<Comment>();

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("comment-section");
        commentService.getComments(threadId, new AsyncCallback<ArrayList<Comment>>() {

            @Override
            public void onSuccess(ArrayList<Comment> oldComments) {
                comments.addAll(oldComments);
                p.add(new WriteBox(new Comment(threadId)));
                if(comments.size() > 0)
                    addNestedComments(new Comment(threadId), 0);
            }

            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });
    }

    private void addNestedComments(Comment parent, int level) {
        for(Comment c : comments) {
            if(c.getParentId() == parent.getId() && c != parent) {
                ReadBox readBox = new ReadBox(c);
                readBox.addStyleName("comment-level-" + (level <= MAX_COMMENT_DEPTH ? level : MAX_COMMENT_DEPTH));
                p.add(readBox);
                addNestedComments(c, level+1);
            }
        }
    }
}