package net.mindlevel.mobile.client.presenter;

import net.mindlevel.mobile.client.places.HomePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;

public class MobileActivityMapper implements ActivityMapper {

    @Inject
    ActivityFactory factory;

    @Inject
    public MobileActivityMapper() {

    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof HomePlace) {
            return factory.createHomePresenter((HomePlace) place);
        }
        return null;
    }

    /**
     * Methods capable of creating presenters given the place that is passed in
     *
     */
    public interface ActivityFactory {
        HomePresenter createHomePresenter(HomePlace place);
    }
}
