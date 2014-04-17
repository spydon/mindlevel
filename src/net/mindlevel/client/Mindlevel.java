package net.mindlevel.client;

import net.mindlevel.client.pages.About;
import net.mindlevel.client.pages.Chat;
import net.mindlevel.client.pages.Highscore;
import net.mindlevel.client.pages.Home;
import net.mindlevel.client.pages.MissionProfile;
import net.mindlevel.client.pages.Missions;
import net.mindlevel.client.pages.Picture;
import net.mindlevel.client.pages.Profile;
import net.mindlevel.client.pages.dialog.Login;
import net.mindlevel.client.pages.dialog.Logout;
import net.mindlevel.client.pages.dialog.Registration;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mindlevel implements EntryPoint, ValueChangeHandler<String> {
    public static User user;
    public static boolean hasKeyUpHandler = false;
    private final String[] pages =
            {"home", "missions", "pictures", "highscore",
            "about", "chat", "login", "logout",
            "register", "profile"};

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        History.addValueChangeHandler(this);
        HandyTools.initTools();
        for(String page:pages)
            connectListener(page);
        new QuoteHandler(RootPanel.get("quote"));
        keepLoggedIn();
        History.fireCurrentHistoryState();
    }

    /**
     * Connects mouse down listeners to HTML-elements.
     */
    private void connectListener(final String name) {
        Element e = RootPanel.get(name).getElement();
        DOM.sinkEvents(e, Event.ONMOUSEDOWN);
        DOM.setEventListener(e, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                String lastPage = History.getToken();
                if((!lastPage.equals("") &&
                    !lastPage.equals("login") &&
                    !lastPage.equals("logout") &&
                    !lastPage.equals("register"))
                    && (name.equals("login") || name.equals("logout"))) {
                    History.newItem(name + "&session=" + lastPage);
                } else if(name.equals("pictures")) { //This should be done in the parser, but how to avoid duplicate picture history? (both #pictures and #picture=3)
                    clearScreen();
                    new Picture(getAppArea(true), 0, true);
                } else {
                    History.newItem(name);
                }
            }
        });
    }

    private void parseToken(String parameters) {
        if(!parameters.contains("=")) {
            if(parameters.equals("home") || parameters.equals("")) { //Empty is always home
                clearScreen();
                new Home(getAppArea(true));
            } else if(parameters.equals("register")) {
                new Registration();
            } else if(parameters.equals("login")) {
                new Login("home");
            } else if(parameters.equals("logout")) {
                clearScreen();
                new Logout("home");
            } else if(parameters.equals("chat")) {
                clearScreen();
                new Chat(getAppArea(false));
            } else if(parameters.equals("profile")) {
                clearScreen();
                new Profile(getAppArea(true), Mindlevel.user.getUsername());
            } else if(parameters.equals("highscore")) {
                clearScreen();
                new Highscore(getAppArea(true));
            } else if(parameters.equals("missions")) {
                clearScreen();
                new Missions(getAppArea(true), true);
            } else if(parameters.equals("pictures")) {
                clearScreen();
                new Picture(getAppArea(true), 0, true);
            } else if(parameters.equals("about")) {
                clearScreen();
                new About(getAppArea(true));
            } else {
                clearScreen();
                new Home(getAppArea(true));
                HandyTools.showDialogBox("Error", new HTML("Something is wrong with your browser history. :( <br />Guru meditation: 1"));
            }
        } else {
            String[] tokens = parameters.split("&");
            boolean validated = true;
            String session = "";
            for(int i = 0; i < tokens.length; i++) {
                if (tokens[i].contains("validated")) {
                    String value = getValue(tokens[i]);
                    validated = !value.toLowerCase().equals("false") && !value.equals("0");
                    i++;
                } else if (tokens[i].contains("session")) {
                    String value = getValue(tokens[i]);
                    session = value.toLowerCase();
                    i++;
                }
            }
            for(int i = 0; i < tokens.length; i++) {
                if(tokens[i].contains("picture")) {
                    try {
                        int pictureId = Integer.parseInt(getValue(tokens[i]));
                        clearScreen();
                        new Picture(getAppArea(true), pictureId, validated);
                    } catch (NumberFormatException nfe) {
                        clearScreen();
                        new Home(getAppArea(true));
                        HandyTools.showDialogBox("Error", new HTML("Couldn't find a picture with that id. :("));
                    }
                    break;
                } else if(tokens[i].contains("user")) {
                    String userId = getValue(tokens[i]).toLowerCase();
                    clearScreen();
                    new Profile(getAppArea(true), userId);
                    break;
                } else if(tokens[i].contains("mission")) {
                    try {
                        int missionId = Integer.parseInt(getValue(tokens[i]));
                        clearScreen();
                        new MissionProfile(getAppArea(true), missionId, validated);
                    } catch (NumberFormatException nfe) {
                        clearScreen();
                        new Home(getAppArea(true));
                        HandyTools.showDialogBox("Error", new HTML("Couldn't find a mission with that id. :("));
                    }
                    break;
                } else if(parameters.contains("login")  && !UserTools.isLoggedIn()) {
                    new Login(session);
                    break;
                } else if(parameters.contains("logout") && UserTools.isLoggedIn()) {
                    clearScreen();
                    new Logout(session);
                    break;
                } else {
                    clearScreen();
                    new Home(getAppArea(true));
                    HandyTools.showDialogBox("Error", new HTML("Something is wrong with your browser history. :( <br /> Guru meditation: 2"));
                }
            }
        }
    }

    public static RootPanel getAppArea(boolean margin) {
        RootPanel appArea = RootPanel.get("apparea");
        if(margin)
            appArea.setStylePrimaryName("margin");
        else
            appArea.setStylePrimaryName("nomargin");
        return appArea;
    }

    private void clearScreen() {
        RootPanel.get("apparea").clear();
    }

    private void keepLoggedIn() {
        if(Cookies.getCookie("mindlevel")!=null)
            UserTools.keepLoggedIn(Cookies.getCookie("mindlevel"));
        else
            UserTools.setLoggedOff();
    }

    private String getValue(String token) {
        return token.substring(token.indexOf("=")+1);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        parseToken(token);
    }
}
