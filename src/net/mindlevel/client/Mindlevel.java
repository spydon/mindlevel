package net.mindlevel.client;

import net.mindlevel.client.pages.About;
import net.mindlevel.client.pages.Chat;
import net.mindlevel.client.pages.Highscore;
import net.mindlevel.client.pages.Home;
import net.mindlevel.client.pages.Login;
import net.mindlevel.client.pages.Logout;
import net.mindlevel.client.pages.MissionProfile;
import net.mindlevel.client.pages.Missions;
import net.mindlevel.client.pages.Picture;
import net.mindlevel.client.pages.Profile;
import net.mindlevel.client.pages.Registration;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mindlevel implements EntryPoint {
    public static User user = new User();
    public static boolean forceFocus = true;
    private final String[] pages =
            {"home", "missions", "pictures", "highscore",
            "about", "chat", "login", "logout",
            "register", "profile"};

    /**
     * Connects mouse down listeners to HTML-elements.
     */
    private void connectListener(final String name) {
        Element e = RootPanel.get(name).getElement();
        DOM.sinkEvents(e, Event.ONMOUSEDOWN);
        DOM.setEventListener(e, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                if(name.equals("home")){
                    clearScreen();
                    new Home(getAppArea(true));
                } else if(name.equals("register")) {
                    forceFocus = false;
                    new Registration();
                } else if(name.equals("login")) {
                    forceFocus = false;
                    new Login();
                } else if(name.equals("logout")) {
                    clearScreen();
                    new Logout(getAppArea(true));
                } else if(name.equals("chat")) {
                    clearScreen();
                    new Chat(getAppArea(false));
                } else if(name.equals("profile")) {
                    clearScreen();
                    new Profile(getAppArea(true), Mindlevel.user.getUsername(), true);
                } else if(name.equals("highscore")) {
                    clearScreen();
                    new Highscore(getAppArea(true));
                } else if(name.equals("missions")) {
                    clearScreen();
                    new Missions(getAppArea(true), true);
                } else if(name.equals("pictures")) {
                    forceFocus = true;
                    clearScreen();
                    new Picture(getAppArea(true), 0, true);
                } else if(name.equals("about")) {
                    clearScreen();
                    new About(getAppArea(true));
                } else {
                    clearScreen();
                    //new ErrorPage();
                }
            }
        });
    }

    public static RootPanel getAppArea(boolean margin) {
        RootPanel appArea = RootPanel.get("apparea");
        //List<String> styles = Arrays.asList(appArea.getStyleName().split(" "));
        if(margin)
            appArea.setStylePrimaryName("margin");
        else
            appArea.setStylePrimaryName("nomargin");
        return appArea;
    }

    private void clearScreen() {
        RootPanel.get("apparea").clear();
    }

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        for(String page:pages)
            connectListener(page);
        new QuoteHandler(RootPanel.get("quote"));
        keepLoggedIn();
        String pictureId = Window.Location.getParameter("picture");
        String userId = Window.Location.getParameter("user");
        String missionId = Window.Location.getParameter("mission");
        boolean validated = Window.Location.getParameter("validated") == null ? true : Window.Location.getParameter("validated").toLowerCase().equals("true");
        if(pictureId!=null)
            try {
                new Picture(getAppArea(true), Integer.parseInt(pictureId), validated);
            } catch (NumberFormatException nfe) {
                new Home(getAppArea(true));
                HandyTools.showDialogBox("Error", new HTML("No picture with that id. :("));
            }
        else if(userId!=null)
            if(userId==Mindlevel.user.getUsername())
                new Profile(getAppArea(true), userId, true);
            else
                new Profile(getAppArea(true), userId, false);
        else if(missionId!=null)
                new MissionProfile(getAppArea(true), Integer.parseInt(missionId), validated);
        else
            new Home(getAppArea(true));
    }

    private void keepLoggedIn() {
        if(Cookies.getCookie("mindlevel")!=null)
            HandyTools.keepLoggedIn(Cookies.getCookie("mindlevel"));
        else
            HandyTools.setLoggedOff();
    }
}
