package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WriteBox extends Composite {

    private final VerticalPanel panel, container;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final CommentServiceAsync commentService = GWT
            .create(CommentService.class);

    /**
     * Constructs an CommentBox with the given caption on the check.
     *
     * @param caption the caption to be displayed with the check box
     */
    public WriteBox(final Comment parent) {
        container = new VerticalPanel();
        panel = new VerticalPanel();
        container.add(panel);

        init(parent);
        // All composites must call initWidget() in their constructors.
        initWidget(container);

        // Give the overall composite a style name.
        setStyleName("comment-box");
    }

    private void init(final Comment parent) {
        final TextArea textArea = new TextArea();
        textArea.addStyleName("comment-textarea");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button reply = new Button("Reply");
        reply.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(UserTools.isLoggedIn()) {
                    panel.clear();
                    final Comment comment = new Comment(parent.getThreadId(),
                                                        UserTools.getUsername(),
                                                        textArea.getText(),
                                                        parent.getId());
                    System.out.println(parent.getThreadId());
                    commentService.addComment(comment, new AsyncCallback<Integer>() {

                        @Override
                        public void onSuccess(Integer id) {
                            comment.setId(id);
                            container.add(new ReadBox(comment));
                        }

                        @Override
                        public void onFailure(Throwable arg0) {
                            HandyTools.showDialogBox("Error", new HTML("Couldn't add comment, try again later."));
                        }
                    });
                } else {
                    HandyTools.notLoggedInBox();
                }
            }
        });

        Button cancel = new Button("Cancel");
        cancel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                removeFromParent();
            }
        });
        buttonPanel.add(reply);
        buttonPanel.add(cancel);
//        buttonPanel.addStyleName("comment-button");

        panel.add(textArea);
        panel.add(buttonPanel);
    }
}