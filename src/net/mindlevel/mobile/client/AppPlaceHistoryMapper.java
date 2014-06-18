package net.mindlevel.mobile.client;

import java.util.HashMap;

import net.mindlevel.mobile.client.places.HomePlace;
import net.mindlevel.mobile.client.places.PicturePlace;

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

        HomePlace homePlace = new HomePlace();
        PicturePlace picturePlace = new PicturePlace();
        placeMapper.put("", homePlace);
        tokenMapper.put(homePlace, "");
        placeMapper.put("picture", picturePlace);
        tokenMapper.put(picturePlace, "picture");
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
