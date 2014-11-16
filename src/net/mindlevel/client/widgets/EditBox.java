package net.mindlevel.client.widgets;

import net.mindlevel.client.services.CommentService;
import net.mindlevel.client.services.CommentServiceAsync;
import net.mindlevel.client.tools.HandyTools;
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

public class EditBox extends Composite {

    private final VerticalPanel panel, container;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final CommentServiceAsync commentService = GWT
            .create(CommentService.class);

    private final TextArea textArea;


    /**
     * Constructs a EditBox editing an existing comment
     *
     * @param comment The comment that should be edited.
     */
    public EditBox(final Comment comment) {
        container = new VerticalPanel();
        panel = new VerticalPanel();
        textArea = new TextArea();
        container.add(panel);

        init(comment);
        // All composites must call initWidget() in their constructors.
        initWidget(container);

        // Give the overall composite a style name.
        setStyleName("comment-box");
    }

    private void init(final Comment comment) {
        textArea.setText(comment.getComment());
        textArea.addStyleName("comment-textarea");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button edit = new Button("Edit");
        edit.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(UserTools.isLoggedIn()) {
                    comment.setComment(textArea.getText());

                    commentService.editComment(comment, UserTools.getToken(), new AsyncCallback<Void>() {

                        @Override
                        public void onSuccess(Void noId) {
                            comment.setComment(textArea.getText());
                            replaceBox(comment);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            HandyTools.notLoggedInBox();
                        }
                    });
                } else {
                    HandyTools.notLoggedInBox();
                }
            }
        });

        Button delete = new Button("Delete");
        delete.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(UserTools.isLoggedIn()) {

                    commentService.deleteComment(comment, UserTools.getToken(), new AsyncCallback<Void>() {

                        @Override
                        public void onSuccess(Void noId) {
                            comment.setComment("Comment deleted by user");
                            replaceBox(comment);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            HandyTools.showDialogBox("Error", new HTML(error.getMessage()));
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
                replaceBox(comment);
            }
        });
        buttonPanel.add(edit);
        buttonPanel.add(delete);
        buttonPanel.add(cancel);
//        buttonPanel.addStyleName("comment-button");

        panel.add(textArea);
        panel.add(buttonPanel);
    }

    protected void replaceBox(Comment comment) {
        ReadBox rb = new ReadBox(comment);

        VerticalPanel commentThread = (VerticalPanel) this.getParent();
        int position = commentThread.getWidgetIndex(this);
        commentThread.insert(rb, position);
        textArea.setText("");
        this.removeFromParent();
    }

}