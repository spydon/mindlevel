package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.shared.Comment;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReadBox extends Composite {

    private final VerticalPanel panel, container;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs an CommentBox with the given caption on the check.
     *
     * @param caption the caption to be displayed with the check box
     */
    public ReadBox(final Comment comment) {
        container = new VerticalPanel();
        panel = new VerticalPanel();
        container.add(panel);
        userService.getUser(comment.getUsername(), new AsyncCallback<User>() {

            @Override
            public void onSuccess(User user) {
                init(user, comment);
            }

            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(container);
        addStyleName("comment-level-" + (comment.getLevel() <= CommentSection.MAX_COMMENT_DEPTH ? comment.getLevel() : CommentSection.MAX_COMMENT_DEPTH));
        panel.setStyleName("comment-box");
    }

    private void init(final User user, final Comment comment) {
        Image picture;
        VerticalPanel rightPanel = new VerticalPanel();
        final VerticalPanel replyPanel = new VerticalPanel();

        if(!user.getThumbnail().equals("")) {
            picture = new Image(Mindlevel.PATH + "pictures/" + user.getThumbnail());
        } else {
            picture = new Image(Mindlevel.PATH + "pictures/" + UserTools.getDefaultThumbnail());
        }
        picture.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                History.newItem("user=" + user.getUsername());
            }
        });
        HTML userLabel = new HTML(HtmlTools.concat("#" + comment.getId() + " ", HtmlTools.getAnchor("user", user.getUsername(), user.getUsername())));
        if(comment.getParentId() != 0) {
            userLabel = new HTML(userLabel.getHTML() + " comments on #" + comment.getParentId());
        }
        Button replyButton = new Button("Reply");
        replyButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if(replyPanel.getWidgetCount() > 0) {
                    HandyTools.showDialogBox("Already replying", new HTML("You are already replying to this comment."));
                } else if(UserTools.isLoggedIn()) {
                    replyPanel.add(new WriteBox(comment));
                    HtmlTools.scrollDown();
                } else {
                    HandyTools.notLoggedInBox();
                }
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();

        if(UserTools.getUsername() == comment.getUsername()) {
            Button editButton = new Button("Edit");
            editButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent arg0) {
                    ReadBox rb = getOuter();
                    VerticalPanel parent = ((VerticalPanel) rb.getParent());
                    parent.insert(new EditBox(comment), parent.getWidgetIndex(rb));
                    parent.remove(rb);
                }
            });
            buttonPanel.add(editButton);
        }

        buttonPanel.add(replyButton);

        HTML commentText = new HTML(HtmlTools.formatHtml(comment.getComment()));
        rightPanel.add(userLabel);
        rightPanel.add(commentText);
        rightPanel.add(buttonPanel);
        rightPanel.addStyleName("comment-content");


        HorizontalPanel readPanel = new HorizontalPanel();
        readPanel.add(picture);
        readPanel.add(rightPanel);
        readPanel.addStyleName("comment-content");

        panel.add(readPanel);
        container.add(replyPanel);

        // Gives all the subwidgets style names
        buttonPanel.setStylePrimaryName("comment-button");
//        editButton.setStylePrimaryName("comment-edit-button");
        picture.addStyleName("comment-thumbnail");
        userLabel.addStyleName("comment-username");
        commentText.addStyleName("comment-text");
    }

    protected ReadBox getOuter() {
        return this;
    }
}