package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PictureSection extends Composite {

    private final VerticalPanel p;

    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public PictureSection(final int number, boolean validated) {
        p = new VerticalPanel();
        HTML header = new HTML("Last finished missions");
        final LoadingElement l = new LoadingElement();
        header.addStyleName("picture-header");
        p.add(header);
        p.add(l);
        Constraint constraint = new Constraint();
        constraint.setValidated(validated);
        constraint.setAdult(UserTools.isAdult());
        constraint.setToken(UserTools.getToken());

        pictureService.getPictures(0, number, constraint,
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
                        p.add(new PictureElement(m, true));
                    }
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("picture-section");
    }
}