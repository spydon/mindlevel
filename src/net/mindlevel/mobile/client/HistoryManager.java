package net.mindlevel.mobile.client;

import java.util.HashMap;

import net.mindlevel.mobile.client.view.HomeView;
import net.mindlevel.mobile.client.view.MPage;
import net.mindlevel.mobile.client.view.PictureView;

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
        pageMapper.put("picture", new PictureView());
    }

    public void parseToken(String token) {
        appArea.clear();
        if(!token.contains("=") && !token.contains("&")) {
            appArea.add(pageMapper.get(token));
        } else {
            String[] parameters = token.split("=");
            MPage page = pageMapper.get(parameters[0]);
            page.setId(parameters[1]);
            appArea.add(page);
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        parseToken(event.getValue());
    }
}
