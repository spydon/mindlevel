package net.mindlevel.mobile.client;

import net.mindlevel.mobile.client.presenter.MobileActivityMapper;
import net.mindlevel.mobile.client.presenter.MobileActivityMapper.ActivityFactory;
import net.mindlevel.mobile.client.view.HomeView;
import net.mindlevel.mobile.client.view.HomeViewImpl;
import net.mindlevel.mobile.client.view.PictureView;
import net.mindlevel.mobile.client.view.PictureViewImpl;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;

public class MindlevelClientModule extends AbstractGinModule {
    @Override
    protected void configure() {
        // Events
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        // A/P, history mapping
        bind(ActivityMapper.class).to(MobileActivityMapper.class);
        bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class);
        bind(HistoryObserver.class).to(AppHistoryObserver.class);
        bind(AnimationMapper.class).to(MobileAnimationMapper.class);

        bind(HomeView.class).to(HomeViewImpl.class);
        bind(PictureView.class).to(PictureViewImpl.class);

        install(new GinFactoryModuleBuilder().build(ActivityFactory.class));
        // Default place to let the app start without history
//        bind(Place.class).annotatedWith(DefaultPlace.class).to(
//                WelcomePlace.class);

        // View interfaces to their singleton Widgets
        // the Widgets themselves are set as singletons
//        bind(WelcomeView.class).to(WelcomeWidget.class);
//        bind(ShowDetailView.class).to(ShowDetailWidget.class);
//        bind(ShowEditorView.class).to(ShowEditorWidget.class);
//        bind(FavoriteShowsListView.class).to(FavoriteShowsListWidget.class);
//
//        bind(LoginView.class).to(LoginWidget.class);//not singleton, since it should only be loaded once
    }

    @Provides
    AnimatingActivityManager provideAnimatingActivityManager(ActivityMapper mapper, AnimationMapper animationMapper, EventBus eventBus) {
        return new AnimatingActivityManager(mapper, animationMapper, eventBus);
    }

    @Provides
    MGWTPlaceHistoryHandler provideMGWTPlaceHistoryHandler(PlaceHistoryMapper placeHistoryMapper, HistoryObserver historyObserver) {
        return new MGWTPlaceHistoryHandler(placeHistoryMapper, historyObserver);
    }

    @Singleton
    @Provides
    PlaceController providePlaceController(EventBus eventBus) {
        return new PlaceController(eventBus);
    }

    @Singleton
    @Provides
    ActivityManager provideActivityManager(ActivityMapper mapper,
            EventBus eventBus) {
        return new ActivityManager(mapper, eventBus);
    }

//    @Singleton
//    @Provides
//    AnimationMapper provideAnimationMapper() {
//        return new PhoneAnimationMapper();
//    }

//    @Singleton
//    @Provides
//    HistoryObserver provideHistoryObserver(ActivityMapper mapper,
//            EventBus eventBus) {
//        return new AppHistoryObserver();
//    }

    @Singleton
    @Provides
    PlaceHistoryHandler providePlaceHistoryHandler(PlaceHistoryMapper mapper,
            PlaceController placeController, EventBus eventBus, //@DefaultPlace
            Place defaultPlace) {
        PlaceHistoryHandler phh = new PlaceHistoryHandler(mapper);
        phh.register(placeController, eventBus, defaultPlace);
        return phh;
    }
}
