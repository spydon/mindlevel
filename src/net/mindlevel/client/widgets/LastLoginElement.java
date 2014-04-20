package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.User;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

public class LastLoginElement extends Composite {

    private final Grid backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public LastLoginElement(User user) {
        backPanel = new Grid(1,2);
        Image image = new Image("pictures/" + user.getThumbnail());
        image.addStyleName("last-login-image");
        HTML username = new HTML(HandyTools.getAnchor("user", user.getUsername(), user.getUsername() + "(" + user.getScore() + ")"));

        backPanel.setWidget(0, 0, image);
        backPanel.setWidget(0, 1, username);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("last-login-element");
    }
}