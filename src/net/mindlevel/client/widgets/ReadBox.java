package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
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
import com.google.gwt.user.client.ui.Label;
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

        // Give the overall composite a style name.
//        setStyleName("comment-box");
        panel.setStyleName("comment-box");
    }

    private void init(final User user, final Comment comment) {
        Image picture;
        VerticalPanel rightPanel = new VerticalPanel();
        final VerticalPanel replyPanel = new VerticalPanel();

        if(!user.getThumbnail().equals(""))
            picture = new Image("./pictures/" + user.getThumbnail());
        else
            picture = new Image("./pictures/" + UserTools.getDefaultThumbnail());
        picture.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                History.newItem("user=" + user.getUsername());
            }
        });
        HTML userLabel = new HTML(HandyTools.getAnchor("user", user.getUsername(), user.getUsername()));
        Button replyButton = new Button("Reply");
        replyButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if(UserTools.isLoggedIn()) {
                    if(replyPanel.getWidgetCount() == 0)
                        replyPanel.add(new WriteBox(comment));
                } else {
                    HandyTools.notLoggedInBox();
                }
            }
        });
        Label commentText = new Label(comment.getComment());
        rightPanel.add(userLabel);
        rightPanel.add(commentText);
        rightPanel.add(replyButton);

        HorizontalPanel readPanel = new HorizontalPanel();
        readPanel.add(picture);
        readPanel.add(rightPanel);

        panel.add(readPanel);
        container.add(replyPanel);

        // Gives all the subwidgets style names
        replyButton.setStylePrimaryName("comment-button");
        picture.addStyleName("comment-thumbnail");
        userLabel.addStyleName("comment-username");
        commentText.addStyleName("comment-label");
    }
}