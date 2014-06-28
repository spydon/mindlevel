package net.mindlevel.mobile.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;

public class MindlevelMobile {

    HistoryManager historyManager;
    private final SimplePanel appArea;

    public MindlevelMobile() {
        appArea = new SimplePanel();
        historyManager = new HistoryManager(appArea);
        onModuleLoad();
    }

    public void onModuleLoad() {
        ViewPort viewPort = new MGWTSettings.ViewPort();
        viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);

        MGWTSettings settings = new MGWTSettings();
        settings.setViewPort(viewPort);
        settings.setIconUrl("./images/logo.png");
        settings.setFullscreen(true);
        settings.setPreventScrolling(true);

        MGWT.applySettings(settings);

        // create an instance of AnimatableDisplay
//        AnimationWidget display = injector.getAnimationWidget();


        // Instantiate your animationMapper
//        MobileAnimationMapper animationMapper = injector.getPhoneAnimationMapper();
        History.addValueChangeHandler(historyManager);

        // add the display to the DOM
//        RootPanel.get().add(display);

        RootPanel.get().add(appArea);

        History.fireCurrentHistoryState();
    }
}
