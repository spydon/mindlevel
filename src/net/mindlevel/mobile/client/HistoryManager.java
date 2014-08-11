package net.mindlevel.mobile.client;

import java.util.HashMap;

import net.mindlevel.mobile.client.view.AboutView;
import net.mindlevel.mobile.client.view.ActivationView;
import net.mindlevel.mobile.client.view.HighscoreView;
import net.mindlevel.mobile.client.view.HomeView;
import net.mindlevel.mobile.client.view.LoginView;
import net.mindlevel.mobile.client.view.MPage;
import net.mindlevel.mobile.client.view.MissionSuggestionView;
import net.mindlevel.mobile.client.view.MissionView;
import net.mindlevel.mobile.client.view.MissionsView;
import net.mindlevel.mobile.client.view.NotFoundView;
import net.mindlevel.mobile.client.view.PictureInfoView;
import net.mindlevel.mobile.client.view.PictureView;
import net.mindlevel.mobile.client.view.RegisterView;
import net.mindlevel.mobile.client.view.ReportView;
import net.mindlevel.mobile.client.view.SearchView;
import net.mindlevel.mobile.client.view.TermsView;
import net.mindlevel.mobile.client.view.TutorialView;
import net.mindlevel.mobile.client.view.UploadView;
import net.mindlevel.mobile.client.view.UserView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;

public class HistoryManager implements ValueChangeHandler<String> {
    private final HashMap<String, MPage> pageMapper;
    private final SimplePanel appArea;

    public HistoryManager(SimplePanel appArea) {
        this.appArea = appArea;

        pageMapper = new HashMap<String, MPage>();

        pageMapper.put("", new HomeView());
        pageMapper.put("about", new AboutView());
        pageMapper.put("activate", new ActivationView());
        pageMapper.put("highscore", new HighscoreView());
        pageMapper.put("login", new LoginView());
        pageMapper.put("mission", new MissionView());
        pageMapper.put("missions", new MissionsView());
        pageMapper.put("missionsuggestion", new MissionSuggestionView());
        pageMapper.put("notfound", new NotFoundView());
        pageMapper.put("picture", new PictureView());
        pageMapper.put("pictureinfo", new PictureInfoView());
        pageMapper.put("register", new RegisterView());
        pageMapper.put("report", new ReportView());
        pageMapper.put("search", new SearchView());
        pageMapper.put("terms", new TermsView());
        pageMapper.put("tutorial", new TutorialView());
        pageMapper.put("upload", new UploadView());
        pageMapper.put("user", new UserView());
    }

    public void parseToken(String token) {
        if(!token.contains("=") && !token.contains("&")) {
            appArea.clear();
            if(pageMapper.containsKey(token)) {
                appArea.add(pageMapper.get(token));
            } else {
                appArea.add(pageMapper.get("notfound"));
            }
        } else {
            String session = "";
            if(token.contains("session")) {
                int sessionIndex = token.indexOf("session");
                int start = sessionIndex+8;
                session = token.substring(start);
                token = token.substring(0, sessionIndex-1);
            }
            String pageName = token.split("=",2)[0];
            String parameters = token.split("=",2).length > 1 ? token.split("=",2)[1] : "";
            if(pageMapper.containsKey(pageName)) {
                appArea.clear();
                MPage page = pageMapper.get(pageName);
                if(parameters.length() > 0) {
                    page.setParameter(parameters);
                }
                page.setSession(session);
                appArea.add(page);
            } else {
                appArea.add(pageMapper.get("notfound"));
            }
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        parseToken(event.getValue());
    }
}
