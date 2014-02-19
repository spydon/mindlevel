package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.shared.Comment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WriteBox extends Composite {

    /**
     * Constructs an CommentBox with the given caption on the check.
     *
     * @param caption the caption to be displayed with the check box
     */
    public WriteBox(final Comment parent) {
        // Place the check above the text box using a vertical panel.
        final VerticalPanel panel = new VerticalPanel();
        final TextArea textArea = new TextArea();
        textArea.addStyleName("comment-textarea");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button reply = new Button("Reply");
        reply.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if(Mindlevel.user != null) {
                    panel.clear();
                    panel.add(new ReadBox(Mindlevel.user, new Comment(0, Mindlevel.user.getUsername(), textArea.getText(), parent.getId(), 123456)));
                } else {
                    HandyTools.showDialogBox("Error", new HTML("You must be logged in to comment<br /><a href=\"#login\">Login</a> or <a href=\"#register\">Register</a>"));
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

        // All composites must call initWidget() in their constructors.
        initWidget(panel);

        // Give the overall composite a style name.
        setStyleName("comment-box");
    }
}