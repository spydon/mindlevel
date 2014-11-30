package net.mindlevel.client.tools;

import java.util.Date;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.pages.Admin;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


public class UserTools {

    private final static String defaultThumbnail = "default_thumb.gif";

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    final static UserServiceAsync userService = GWT
            .create(UserService.class);

    public static boolean isLoggedIn() {
        return Mindlevel.user != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && Mindlevel.user.isAdmin();
    }

    public static boolean isModerator() {
        return isLoggedIn() && Mindlevel.user.isModerator();
    }

    public static boolean isAdult() {
        return isLoggedIn() && Mindlevel.user.isAdult();
    }

    public static void setLoggedIn(User user) {
        Mindlevel.user = user;
        Date date = new Date();
        long nowLong = date.getTime();
        nowLong = nowLong + (1000 * 60 * 60 * 24 * 7);//seven days
        date.setTime(nowLong);
        Cookies.setCookie("mindlevel", user.getToken(), date);
        HandyTools.setRightView(true, user);
        if(user.isAdmin() && Mindlevel.isDesktop()) {
            new Admin();
        }
    }

    public static void setLoggedOff() {
        Cookies.removeCookie("mindlevel");
        Mindlevel.user = null;
        RootPanel.get("chat-frame").getElement().setAttribute("src", "");
        HandyTools.setRightView(false, null);
    }

    public static void keepLoggedIn(String token) {
        userService.getUserFromToken(token, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                setLoggedOff();
                History.fireCurrentHistoryState();
            }

            @Override
            public void onSuccess(User user) {
                setLoggedIn(user);
                History.fireCurrentHistoryState();
            }
        });
    }

    /**
     * Returns the username of the user currently logged in.
     * @return username
     */
    public static String getUsername() {
        String username = isLoggedIn() ? Mindlevel.user.getUsername() : "";
        return username;
    }

    public static String getToken() {
        return Mindlevel.user == null ? "" : Mindlevel.user.getToken();
    }

    public static String getDefaultThumbnail() {
        return defaultThumbnail;
    }
}