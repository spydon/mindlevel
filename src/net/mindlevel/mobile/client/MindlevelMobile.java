package net.mindlevel.mobile.client;

import net.mindlevel.mobile.client.places.HomePlace;
import net.mindlevel.mobile.client.presenter.MobileActivityMapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.widget.animation.AnimationWidget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MindlevelMobile {

    public MindlevelMobile() {
        onModuleLoad();
    }

    public void onModuleLoad() {
        RootPanel.get("topheader").setStyleName("superhidden", true);
        ViewPort viewPort = new MGWTSettings.ViewPort();
        viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);

        MGWTSettings settings = new MGWTSettings();
        settings.setViewPort(viewPort);
        settings.setIconUrl("./images/logo.png");
        settings.setFullscreen(true);
        settings.setPreventScrolling(true);

        final MindlevelInjector injector = GWT.create(MindlevelInjector.class);
        injector.inject(this);

        MGWT.applySettings(settings);

        // create an instance of AnimatableDisplay
        AnimationWidget display = injector.getAnimationWidget();

        // Instantiate your activity mapper
        MobileActivityMapper activityMapper = injector.getPhoneActivityMapper();

        // Instantiate your animationMapper
        MobileAnimationMapper animationMapper = injector.getPhoneAnimationMapper();

        // setup an activity manager for the display
        AnimatingActivityManager activityManager =
                injector.getAnimatingActivityManager();

        // pass the display to the activity manager
        activityManager.setDisplay(display);

        // add the display to the DOM
        RootPanel.get().add(display);
        MGWTPlaceHistoryHandler historyHandler = injector.getMGWTPlaceHistoryHandler();

        historyHandler.register(injector.getPlaceController(), injector.getEventBus(), new HomePlace());
        historyHandler.handleCurrentHistory();
    }
}
