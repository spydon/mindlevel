package net.mindlevel.mobile.client;

import java.util.HashMap;

import net.mindlevel.mobile.client.places.HomePlace;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper {

    private final HashMap<String, Place> placeMapper;
    private final HashMap<Place, String> tokenMapper;

    @Inject
    public AppPlaceHistoryMapper() {
        placeMapper = new HashMap<String, Place>();
        tokenMapper = new HashMap<Place, String>();

        HomePlace home = new HomePlace();
        placeMapper.put("", home);
        tokenMapper.put(home, "");
    }

    @Override
    public Place getPlace(String token) {
        return placeMapper.get(token);
    }

    @Override
    public String getToken(Place place) {
        return tokenMapper.get(place);
    }

}
