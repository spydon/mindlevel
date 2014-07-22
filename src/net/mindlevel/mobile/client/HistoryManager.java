package net.mindlevel.mobile.client;

import java.util.HashMap;

import net.mindlevel.client.UserTools;
import net.mindlevel.mobile.client.view.AboutView;
import net.mindlevel.mobile.client.view.HighscoreView;
import net.mindlevel.mobile.client.view.HomeView;
import net.mindlevel.mobile.client.view.LoginView;
import net.mindlevel.mobile.client.view.MPage;
import net.mindlevel.mobile.client.view.MissionView;
import net.mindlevel.mobile.client.view.MissionsView;
import net.mindlevel.mobile.client.view.PictureView;
import net.mindlevel.mobile.client.view.RegisterView;
import net.mindlevel.mobile.client.view.TermsView;
import net.mindlevel.mobile.client.view.UserView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.mgwt.ui.client.animation.AnimationHelper;

public class HistoryManager implements ValueChangeHandler<String> {

    private final AnimationHelper animationHelper;

    private final HashMap<String, MPage> pageMapper;
    private final SimplePanel appArea;

    public HistoryManager(SimplePanel appArea) {
        this.appArea = appArea;
        this.animationHelper = new AnimationHelper();

        pageMapper = new HashMap<String, MPage>();

        pageMapper.put("", new HomeView());
        pageMapper.put("picture", new PictureView());
        pageMapper.put("login", new LoginView());
        pageMapper.put("highscore", new HighscoreView());
        pageMapper.put("missions", new MissionsView());
        pageMapper.put("mission", new MissionView());
        pageMapper.put("user", new UserView());
        pageMapper.put("about", new AboutView());
        pageMapper.put("register", new RegisterView());
        pageMapper.put("terms", new TermsView());
    }

    public void parseToken(String token) {
        if(!token.contains("=") && !token.contains("&")) {
            if (token.equals("logout")) {
                UserTools.setLoggedOff();
                token = "";
            }

            if(token.equals("")) {
                ((HomeView) pageMapper.get("")).setLoggedIn(UserTools.isLoggedIn());
            }

            appArea.clear();
            appArea.add(pageMapper.get(token));
        } else {
            String session = "";
            if(token.contains("session")) {
                int start = token.indexOf("session")+7;
                session = token.substring(start);
            }
            String[] parameters = token.split("=");
            if(pageMapper.containsKey(parameters[0])) {
                appArea.clear();
                MPage page = pageMapper.get(parameters[0]);
                page.setId(parameters[1]);
                page.setSession(session);
                appArea.add(page);
            }
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        parseToken(event.getValue());
    }
}
