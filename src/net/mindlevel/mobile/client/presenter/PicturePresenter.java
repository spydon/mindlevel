package net.mindlevel.mobile.client.presenter;

import net.mindlevel.mobile.client.places.PicturePlace;
import net.mindlevel.mobile.client.view.PictureView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PicturePresenter extends AbstractActivity implements PictureView.Presenter {

    @Inject
    PictureView view;
    @Inject
    PlaceController placeController;

    @Inject
    public PicturePresenter(@Assisted
    PicturePlace place) {
        //place is meaningless, this is just a simple view to show
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view);
    }
}
