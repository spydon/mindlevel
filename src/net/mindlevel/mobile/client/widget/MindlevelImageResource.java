package net.mindlevel.mobile.client.widget;

import net.mindlevel.client.Mindlevel;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

public class MindlevelImageResource implements ImageResource {
    private final String image = Mindlevel.PATH + "images/icons/icon.svg";
    @Override
    public String getName() {
        return image.substring(image.lastIndexOf("/"), image.indexOf("."));
    }

    @Override
    public int getHeight() {
        return 32;
    }

    @Override
    public int getLeft() {
        return 0;
    }

    @Override
    public SafeUri getSafeUri() {
        return UriUtils.fromString(image);
    }

    @Override
    public int getTop() {
        return 0;
    }

    @Override
    public String getURL() {
        return image;
    }

    @Override
    public int getWidth() {
        return 32;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }
}