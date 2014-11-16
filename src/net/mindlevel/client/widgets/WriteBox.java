package net.mindlevel.client.widgets;

import net.mindlevel.client.services.CommentService;
import net.mindlevel.client.services.CommentServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.client.tools.UserTools;
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
     * Constructs a WriteBox answering the parent comment
     *
     * @param parent the parent to be answered
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
        if(parent.getId() == 0) {
            addStyleName("comment-level-0");
        }
    }

    private void init(final Comment parent) {
        final TextArea textArea = new TextArea();
        textArea.addStyleName("comment-textarea");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button reply = new Button("Comment");
        reply.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(textArea.getText().equals("")) {
                    HandyTools.showDialogBox("No text", new HTML("Your comment is empty..."));
                } else if(UserTools.isLoggedIn()) {
                    final Comment comment = new Comment(parent.getThreadId(),
                                                        UserTools.getUsername(),
                                                        textArea.getText(),
                                                        parent.getId());
                    commentService.addComment(comment, UserTools.getToken(), new AsyncCallback<Integer>() {

                        @Override
                        public void onSuccess(Integer id) {
                            comment.setId(id);
                            if(parent.getId() == 0) {
                                comment.setLevel(0);
                            } else {
                                comment.setLevel(1);
                            }

                            ReadBox rb = new ReadBox(comment);
                            container.add(rb);
                            VerticalPanel commentThread = (VerticalPanel) container.getParent().getParent();
                            int position = commentThread.getWidgetIndex(container.getParent());
                            commentThread.insert(rb, position+1);
                            textArea.setText("");
                            if(parent.getId() != 0) {
                                container.clear();
                                container.getParent().removeFromParent();
                            }
                            HtmlTools.scrollDown();
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
        if(parent.getId() != 0) {
            buttonPanel.add(cancel);
        }
//        buttonPanel.addStyleName("comment-button");

        panel.add(textArea);
        panel.add(buttonPanel);
    }
}