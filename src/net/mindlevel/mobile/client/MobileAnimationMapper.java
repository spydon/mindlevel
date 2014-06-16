package net.mindlevel.mobile.client;

import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.googlecode.mgwt.ui.client.widget.animation.Animation;
import com.googlecode.mgwt.ui.client.widget.animation.Animations;


public class MobileAnimationMapper implements AnimationMapper {

    @Inject
    public MobileAnimationMapper() {
    }

    @Override
    public Animation getAnimation(Place oldPlace, Place newPlace) {
//            if (oldPlace instanceof AboutPlace && newPlace instanceof HomePlace) {
//                    return Animation.SLIDE;
//            }
            return Animations.FADE;
    }
}