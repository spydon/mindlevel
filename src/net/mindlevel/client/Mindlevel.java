package net.mindlevel.client;

import net.mindlevel.client.pages.About;
import net.mindlevel.client.pages.Activate;
import net.mindlevel.client.pages.Chat;
import net.mindlevel.client.pages.Highscore;
import net.mindlevel.client.pages.Home;
import net.mindlevel.client.pages.MissionProfile;
import net.mindlevel.client.pages.Missions;
import net.mindlevel.client.pages.NotFound;
import net.mindlevel.client.pages.Picture;
import net.mindlevel.client.pages.Profile;
import net.mindlevel.client.pages.Search;
import net.mindlevel.client.pages.Terms;
import net.mindlevel.client.pages.Tutorial;
import net.mindlevel.client.pages.dialog.Login;
import net.mindlevel.client.pages.dialog.Logout;
import net.mindlevel.client.pages.dialog.MissionSuggestion;
import net.mindlevel.client.pages.dialog.Registration;
import net.mindlevel.client.pages.dialog.ReportBox;
import net.mindlevel.client.pages.dialog.SearchBox;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.client.widgets.FooterElement;
import net.mindlevel.client.widgets.QuoteElement;
import net.mindlevel.client.widgets.SearchElement;
import net.mindlevel.mobile.client.MindlevelMobile;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.SearchType;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.ui.client.MGWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mindlevel implements EntryPoint, ValueChangeHandler<String> {
    public static User user;
    public static HandlerRegistration navigationHandlerRegistration = null;
    public static String PATH = "./";
    private static boolean isDesktop = true;

    private int pictureId = 0;
    private final String[] pages =
            {"home", "missions", "pictures", "highscore",
            "about", "chat", "login", "register", "search"};

    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    /**
     * This is the entry point method.
     */
    @Override
    public void onModuleLoad() {
        if(!MGWT.getFormFactor().isDesktop() && !MGWT.getOsDetection().isAndroid2x() ) {
            isDesktop = false;
        }

        if(Cookies.getCookie("platform") != null && Cookies.getCookie("platform").equals("mobile")) {
            isDesktop = false;
        } else if(Cookies.getCookie("platform") != null && Cookies.getCookie("platform").equals("desktop")) {
            isDesktop = true;
        }

        if(isDesktop()) {
            RootPanel.get().addStyleName("desktop");
            updatePictureId();
            Document.get().getElementById("topheader").removeClassName("superhidden");
            History.addValueChangeHandler(this);
            HandyTools.initTools();
            for(String page : pages) {
                connectListener(page);
            }
            FooterElement footer = new FooterElement();
//            RootPanel.get("apparea").add(footer, 0, RootPanel.get("apparea").getOffsetHeight()-footer.getOffsetHeight());
            RootPanel.get("apparea").add(footer);
            RootPanel.get("search").add(new SearchElement());
        } else {
            RootPanel.get("apparea").setVisible(false);
            RootPanel.get().addStyleName("mobile");
            Document.get().getElementById("topheader").addClassName("superhidden");
            new MindlevelMobile();
        }
        keepLoggedIn();
    }

    /**
     * Connects mouse down listeners to HTML-elements.
     */
    private void connectListener(final String name) {
        Element e = Document.get().getElementById(name);
        DOM.sinkEvents(e, Event.ONMOUSEUP);
        DOM.setEventListener(e, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                String lastPage = History.getToken();
                if(lastPage.equals(name)) {
                    History.fireCurrentHistoryState();
                } else if((!lastPage.equals("") &&
                    !lastPage.equals("login") &&
                    !lastPage.equals("logout") &&
                    !lastPage.equals("register") &&
                    !lastPage.equals("search"))
                    && (name.equals("login") || name.equals("logout"))) {
                    History.newItem(name + "&session=" + lastPage);
                } else if(name.equals("pictures")) {
                    updatePictureId();
                    History.newItem("picture=" + pictureId);
                } else if(name.equals("home")) {
                    History.newItem("");
                } else {
                    History.newItem(name);
                }
            }
        });
    }

    private void parseToken(String parameters) {
        RootPanel.get("chat-frame").setStyleName("superhidden", true);
        if(!parameters.contains("=")) {
            if(parameters.equals("")) { //Empty is always home
                clearScreen();
                new Home();
            } else if(parameters.equals("register")) {
                new Registration();
            } else if(parameters.equals("login")) {
                new Login("");
            } else if(parameters.equals("search")) {
                new SearchBox();
            } else if(parameters.equals("logout")) {
                clearScreen();
                new Logout("");
            } else if(parameters.equals("chat")) {
                clearScreen();
                ;
                new Chat();
            } else if(parameters.equals("highscore")) {
                clearScreen();
                new Highscore();
            } else if(parameters.equals("missions")) {
                clearScreen();
                Constraint constraint = new Constraint();
                constraint.setAdult(UserTools.isAdult());
                new Missions(constraint);
            } else if(parameters.equals("pictures")) {
                clearScreen();
                new Picture(0, true);
            } else if(parameters.equals("missionsuggestion")) {
                if(UserTools.isLoggedIn()) {
                    new MissionSuggestion();
                } else {
                    HandyTools.notLoggedInBox();
                }
            } else if(parameters.equals("about")) {
                clearScreen();
                new About();
            } else if(parameters.equals("terms")) {
                clearScreen();
                new Terms();
            } else if(parameters.equals("tutorial")) {
                clearScreen();
                new Tutorial();
            } else if(parameters.equals("report")) {
                new ReportBox();
            } else if(parameters.equals("notfound")) {
                clearScreen();
                new NotFound();
            } else {
                clearScreen();
                new NotFound();
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
                if(tokens[i].startsWith("picture")) {
                    try {
                        int pictureId = Integer.parseInt(getValue(tokens[i]));
                        clearScreen();
                        new Picture(pictureId, validated);
                    } catch (NumberFormatException nfe) {
                        clearScreen();
                        new Home();
                        HandyTools.showDialogBox("Error", new HTML("Couldn't find a picture with that id. :("));
                    }
                    break;
                } else if(tokens[i].startsWith("user")) {
                    String userId = getValue(tokens[i]).toLowerCase();
                    clearScreen();
                    new Profile(userId);
                    break;
                } else if(tokens[i].startsWith("mission")) {
                    try {
                        int missionId = Integer.parseInt(getValue(tokens[i]));
                        clearScreen();
                        new MissionProfile(missionId, validated);
                    } catch (NumberFormatException nfe) {
                        clearScreen();
                        new Home();
                        HandyTools.showDialogBox("Error", new HTML("Couldn't find a mission with that id. :("));
                    }
                    break;
                } else if(parameters.startsWith("activate")) {
                    String uuid = getValue(tokens[i]);
                    new Activate(uuid);
                    break;
                } else if(parameters.startsWith("search")) {
                    Constraint constraint = new Constraint();
                    constraint.setType(SearchType.valueOf(getValue(tokens[0]).toUpperCase()));
                    for(int j = 0; j < tokens.length; j++) {
                        if(tokens[j].startsWith("c=")) {
                            try {
                                constraint.setCategory(Category.valueOf(getValue(tokens[j]).toUpperCase()));
                            } catch(IllegalArgumentException e) {
                                constraint.setCategory(Category.ALL);
                            }
                        } else if(tokens[j].startsWith("u=")) {
                            constraint.setUsername(getValue(tokens[j]));
                        } else if(tokens[j].startsWith("p=")) {
                            constraint.setPictureTitle(getValue(tokens[j]));
                        } else if(tokens[j].startsWith("m=")) {
                            constraint.setMissionName(getValue(tokens[j]));
                        }
                    }
                    constraint.setAdult(UserTools.isAdult());
                    constraint.setToken(UserTools.getToken());
                    clearScreen();
                    new Search("Search", constraint);

                } else if(parameters.startsWith("colour")) {
                    RootPanel.getBodyElement().getStyle().setBackgroundColor("#" + getValue(tokens[0]));
                    clearScreen();
                    new Home();
                } else if(parameters.startsWith("login")  && !UserTools.isLoggedIn()) {
                    new Login(session);
                    break;
                } else if(parameters.startsWith("logout") && UserTools.isLoggedIn()) {
                    clearScreen();
                    new Logout(session);
                    break;
                } else {
                    clearScreen();
                    new NotFound();
                }
            }
        }
    }

    private void clearScreen() {
        RootPanel.get("apparea").clear();
    }

    private void keepLoggedIn() {
        if(Cookies.getCookie("mindlevel")!=null) {
            UserTools.keepLoggedIn(Cookies.getCookie("mindlevel"));
        } else {
            UserTools.setLoggedOff();
            History.fireCurrentHistoryState(); //TODO: is this needed?
        }
    }

    private void updatePictureId() {
        pictureService.get(0, true, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {

            @Override
            public void onFailure(Throwable caught) {
                //Do nothing
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                pictureId = metaImage.getId();
            }
        });
    }

    public static boolean isDesktop() {
        return isDesktop;
    }

    private String getValue(String token) {
        return token.substring(token.indexOf("=")+1);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        parseToken(event.getValue());
    }
}
