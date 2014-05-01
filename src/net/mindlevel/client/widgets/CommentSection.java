package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.CommentService;
import net.mindlevel.client.services.CommentServiceAsync;
import net.mindlevel.shared.Comment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
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

        p.add(new WriteBox(new Comment(threadId)));

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        commentService.getCommentCount(threadId, new AsyncCallback<Integer>() {

            @Override
            public void onSuccess(Integer count) {
                if(count > 0) {
                    final Button b = new Button("Show comments (" + count + ")");
                    b.setStylePrimaryName("comment-button-show-all");
                    b.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent arg0) {
                            final LoadingElement l = new LoadingElement();
                            b.removeFromParent();
                            p.add(l);
                            commentService.getComments(threadId, new AsyncCallback<ArrayList<Comment>>() {

                                @Override
                                public void onSuccess(ArrayList<Comment> oldComments) {
                                    l.removeFromParent();
                                    comments.addAll(oldComments);
                                    addNestedComments(new Comment(threadId), 0);
                                    HandyTools.scrollDown();
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    l.removeFromParent();
                                    HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                                }
                            });
                        }
                    });
                    p.add(b);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });

        // Give the overall composite a style name.
        setStyleName("comment-section");
    }

    //Use iterator
    private void addNestedComments(Comment parent, int level) {
        for(Comment c : comments) {
            if(c.getParentId() == parent.getId() && c != parent) {
                c.setLevel(level);
                ReadBox readBox = new ReadBox(c);
                p.add(readBox);
                addNestedComments(c, level+1);
            }
        }
    }

    public CommentSection getOuter() {
        return this;
    }
}