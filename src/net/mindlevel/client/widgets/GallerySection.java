package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class GallerySection extends Composite {

    private final FlowPanel p;

    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    /**
     * Constructs a GallerySection that controls a number of PictureElements
     *
     */
    public GallerySection(final Constraint constraint) {
        p = new FlowPanel();
        final LoadingElement l = new LoadingElement();
        p.add(l);

        pictureService.getPictures(0, 20, constraint,
                new AsyncCallback<ArrayList<MetaImage>>() {

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(ArrayList<MetaImage> pictures) {
                if(pictures.size() > 0) {
                    for(MetaImage m : pictures) {
                        p.add(new PictureElement(m, false));
                    }
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("gallery-section");
    }
}