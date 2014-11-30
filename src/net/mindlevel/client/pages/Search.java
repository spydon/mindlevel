package net.mindlevel.client.pages;

import net.mindlevel.client.pages.dialog.SearchBox;
import net.mindlevel.client.widgets.GallerySection;
import net.mindlevel.client.widgets.MissionSection;
import net.mindlevel.client.widgets.UserSection;
import net.mindlevel.shared.Constraint;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Search extends Page {
    private final VerticalPanel backPanel;
    private final String title;
    private final Constraint constraint;

    public Search(String title, Constraint constraint) {
        super();
        this.backPanel = new VerticalPanel();
        this.title = title;
        this.constraint = constraint;
        backPanel.addStyleName("fullwidth");
        init();
    }

    protected void init() {
        backPanel.add(new HTML("<h1>" + title + "</h1>"));
        appArea.add(backPanel);
        switch(constraint.getType()) {
        case ALL:
            new SearchBox();
        case MISSION:
            missionSearch(constraint);
            break;
        case PICTURE:
            pictureSearch(constraint);
            break;
        case USER:
            userSearch(constraint);
            break;
        }
    }

    private void userSearch(Constraint constraint) {
        UserSection usersSection = new UserSection(constraint);
        backPanel.add(usersSection);
    }

    private void pictureSearch(Constraint constraint) {
        GallerySection gallery = new GallerySection(constraint);
        backPanel.add(gallery);
    }

    private void missionSearch(Constraint constraint) {
        MissionSection missionSection = new MissionSection(constraint);
        backPanel.add(missionSection);
    }
}
