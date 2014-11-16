package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

public class LoadingElement extends Composite {

    public static String loadingPathSmall = Mindlevel.PATH + "images/loading4.gif";
    public static String loadingPathLong = Mindlevel.PATH + "images/loading.gif";

    public static enum SIZE {SMALL, LONG}

    public LoadingElement() {
        this(SIZE.SMALL);
    }

    /**
     * Constructs an panel with a loading gif attached to it
     *
     */
    public LoadingElement(SIZE size) {
        Image loadingImage;
        switch (size) {
        case SMALL:
            loadingImage = new Image(loadingPathSmall);
            break;
        case LONG:
            loadingImage = new Image(loadingPathLong);
            break;
        default:
            loadingImage = new Image(loadingPathSmall);
            break;
        }
        initWidget(loadingImage);
        setStyleName("loading-element");
    }
}