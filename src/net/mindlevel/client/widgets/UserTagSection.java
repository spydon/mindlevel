package net.mindlevel.client.widgets;

import java.util.HashSet;

import net.mindlevel.client.widgets.UserTagElement.SIZE;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class UserTagSection extends Composite {

    /**
     * Constructs an UserTagSection with the given users displayed.
     *
     */
    public UserTagSection(HashSet<String> tags, final boolean hasPicture, final boolean hasName, final SIZE size) {
        FlowPanel p = new FlowPanel();
        for(String tag : tags) {
            p.add(new UserTagElement(tag, hasPicture, hasName, size));
        }

        initWidget(p);
        setStyleName("user-tag-section");
    }
}