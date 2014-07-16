package net.mindlevel.mobile.client;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.googlecode.mgwt.ui.client.util.ImageLoader;
import com.googlecode.mgwt.ui.client.util.IsImage;

public class MetaImageElement extends Image {
    private MetaImage metaImage;

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
                setUrl(loaded.getElement().getSrc());
            }
        });
    }

    public MetaImage getMetaImage() {
        return metaImage;
    }

    public void setMetaImage(MetaImage metaImage) {
        this.metaImage = metaImage;
    }
}
