package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.shared.Comment;
import net.mindlevel.shared.User;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ReadBox extends Composite {

    /**
     * Constructs an CommentBox with the given caption on the check.
     *
     * @param caption the caption to be displayed with the check box
     */
    public ReadBox(final User user, final Comment comment) {
        VerticalPanel rightPanel = new VerticalPanel();
        final VerticalPanel replyPanel = new VerticalPanel();
        Image picture = new Image("./pictures/" + user.getThumbnail());
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
                if(Mindlevel.user != null) {
                    if(replyPanel.getWidgetCount() == 0)
                        replyPanel.add(new WriteBox(comment));
                } else {
                    HandyTools.showDialogBox("Error", new HTML("You must be logged in to comment<br /><a href=\"#login\">Login</a> or <a href=\"#register\">Register</a>"));
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

        VerticalPanel panel = new VerticalPanel();
        panel.add(readPanel);
        panel.add(replyPanel);

        // All composites must call initWidget() in their constructors.
        initWidget(panel);

        // Gives all the subwidgets style names
        replyButton.setStylePrimaryName("comment-button");
        picture.addStyleName("comment-thumbnail");
        userLabel.addStyleName("comment-username");
        commentText.addStyleName("comment-label");

        // Give the overall composite a style name.
        setStyleName("comment-box");

    }
}