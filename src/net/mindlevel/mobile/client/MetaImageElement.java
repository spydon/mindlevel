package net.mindlevel.mobile.client;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.ui.client.util.ImageLoader;
import com.googlecode.mgwt.ui.client.util.IsImage;

public class MetaImageElement extends Image {
    private MetaImage metaImage;
    private int width = 0;
    private int height = 0;

    private boolean isLoaded = false;

    public MetaImageElement(MetaImage metaImage) {
//        addStyleName("m-image-panel");
        setMetaImage(metaImage);
        init();
    }

    private void init() {
        removeStyleName("m-image");
        setUrl(LoadingElement.loadingPath);

        new ImageLoader().loadImage(Mindlevel.PATH + "pictures/" + metaImage.getFilename(), new AsyncCallback<IsImage>() {

            @Override
            public void onFailure(Throwable arg0) {
                //Report that something went wrong
            }

            @Override
            public void onSuccess(IsImage loaded) {
                addStyleName("m-image");
                width = loaded.getElement().getWidth();
                height = loaded.getElement().getHeight();

                setUrl(loaded.getElement().getSrc());
                adjustSize();
                isLoaded = true;
            }
        });
    }

    public void adjustSize() {
        int clientWidth =  Window.getClientWidth();
        int clientHeight = Window.getClientHeight()-45;
        int newWidth = 0;
        int newHeight = 0;

        if((double)width/clientWidth > (double)height/clientHeight) {
            newWidth = clientWidth;
            newHeight = height*newWidth/width;
            getElement().getStyle().setMarginTop((clientHeight-newHeight)/2, Unit.PX);
        } else {
            newHeight = clientHeight;
            newWidth = newHeight*width/height;
        }

        width = newWidth;
        height = newHeight;

        setPixelSize(width, height);
    }

    public MetaImage getMetaImage() {
        return metaImage;
    }

    public void setMetaImage(MetaImage metaImage) {
        this.metaImage = metaImage;
    }

    public boolean isLoaded() {
        return isLoaded;
    }
}
