package net.mindlevel.mobile.client.widget;

import net.mindlevel.client.Mindlevel;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.googlecode.mgwt.ui.client.MGWT;

public class MindlevelImageResource implements ImageResource {
    private final String image = Mindlevel.PATH + "images/icons/icon.svg";
    private final int iconWidth = 32;
    private final int iconHeight = 32;
    @Override
    public String getName() {
        return image.substring(image.lastIndexOf("/"), image.indexOf("."));
    }

    @Override
    public int getHeight() {
        int height = iconHeight;
        if(MGWT.getDeviceDensity().isHighDPI()) {
            height *= 1.5;
        } else if(MGWT.getDeviceDensity().isXHighDPI()){
            height *= 2;
        }
        return height;
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
        int width = iconWidth;
        if(MGWT.getDeviceDensity().isHighDPI()) {
            width *= 1.5;
        } else if(MGWT.getDeviceDensity().isXHighDPI()){
            width *= 2;
        }
        return width;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }
}