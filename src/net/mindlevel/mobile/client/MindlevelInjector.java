package net.mindlevel.mobile.client;

import net.mindlevel.mobile.client.presenter.MobileActivityMapper;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.widget.animation.AnimationWidget;

@GinModules(MindlevelClientModule.class)
public interface MindlevelInjector extends Ginjector {
    EventBus getEventBus();
    AnimationWidget getAnimationWidget();
    MobileActivityMapper getPhoneActivityMapper();
    MobileAnimationMapper getPhoneAnimationMapper();
    AppPlaceHistoryMapper getAppPlaceHistoryMapper();
    AppHistoryObserver getAppHistoryObserver();
    AnimatingActivityManager getAnimatingActivityManager();
    MGWTPlaceHistoryHandler getMGWTPlaceHistoryHandler();
    PlaceController getPlaceController();

    /** Provide injection for the entrypoint */
    void inject(MindlevelMobile mindlevelMobile);
}
