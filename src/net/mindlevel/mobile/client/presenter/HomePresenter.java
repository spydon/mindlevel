package net.mindlevel.mobile.client.presenter;

import net.mindlevel.mobile.client.places.HomePlace;
import net.mindlevel.mobile.client.view.HomeView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class HomePresenter extends AbstractActivity implements HomeView.Presenter {

    @Inject
    HomeView view;
    @Inject
    PlaceController placeController;

    @Inject
    public HomePresenter(@Assisted
    HomePlace place) {
        //place is meaningless, this is just a simple view to show
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view);
    }
}
