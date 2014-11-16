package net.mindlevel.client.widgets;

import java.util.List;

import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class HighscoreSection extends Composite {

    private final FlowPanel p;

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public HighscoreSection(final int number) {
        p = new FlowPanel();
        HTML header = new HTML("Highscore");
        final LoadingElement l = new LoadingElement(LoadingElement.SIZE.LONG);
        header.addStyleName("users-header");
        p.add(header);
        p.add(l);


        userService.getHighscore(0, number, new AsyncCallback<List<User>>() {

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<User> users) {
                if(users.size() > 0) {
                    for(User u : users) {
                        HorizontalPanel h = new HorizontalPanel();

                        int place = users.indexOf(u)+1;
                        HTML placeHolder = new HTML("<h1>" + place + "</h1>");

                        if(place == 1) {
                            placeHolder.addStyleName("gold");
                        } else if (place == 2) {
                            placeHolder.addStyleName("silver");
                        } else if (place == 3) {
                            placeHolder.addStyleName("bronze");
                        }

                        h.addStyleName("m-middle");
                        UserElement userElement = new UserElement(u, true);
                        userElement.addStyleName("m-user-element");

                        h.add(placeHolder);
                        h.add(userElement);
                        p.add(h);
                    }
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("users-section");
    }
}