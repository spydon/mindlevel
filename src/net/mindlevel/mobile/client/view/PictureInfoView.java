package net.mindlevel.mobile.client.view;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.mobile.client.MetaImageElement;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPanel;


public class PictureInfoView extends MPage {
    private final FlexPanel main;
    private final SimplePanel picturePanel, loadingPanel, descriptionContainer;
    private final VerticalPanel infoPanel;
    private final HTML title, description, location, uploader, tags, date, mission, category, link, score;
    private int id = 0;

    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    /**
     * Create a remote service proxy to talk to the server-side mission
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);


    public PictureInfoView() {
        main = new FlexPanel();
        loadingPanel = new SimplePanel();

        title = new HTML();
        location = new HTML();
        uploader = new HTML();
        description = new HTML();
        mission = new HTML();
        category = new HTML();
        tags = new HTML();
        score = new HTML();
        link = new HTML();
        date = new HTML();

        picturePanel = new SimplePanel();
        picturePanel.addStyleName("m-image-container");

        descriptionContainer = new SimplePanel();
        descriptionContainer.addStyleName("m-description-panel");
        descriptionContainer.add(description);

        infoPanel = new VerticalPanel();
        infoPanel.addStyleName("m-info-panel");
        infoPanel.add(uploader);
        infoPanel.add(score);
        infoPanel.add(location);
        infoPanel.add(mission);
        infoPanel.add(category);
        infoPanel.add(tags);
        infoPanel.add(link);
        infoPanel.add(date);

        main.add(picturePanel);
        main.add(loadingPanel);
        main.add(title);
        main.add(infoPanel);
        main.add(descriptionContainer);
    }

    private void init() {
        picturePanel.clear();
        setVisible(false);
        loadingPanel.add(new LoadingElement());
        pictureService.get(id, false, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(MetaImage metaImage) {
                picturePanel.add(new MetaImageElement(metaImage, true));
                initInfo(metaImage);
            }
        });
    }

    private void setVisible(boolean visible) {
        infoPanel.setVisible(visible);
        descriptionContainer.setVisible(visible);
        title.setVisible(visible);
    }

    private void initInfo(MetaImage metaImage) {
        location.setHTML("<b>Location: </b>" + metaImage.getLocation());
        uploader.setHTML("<b>Uploader: </b>" + HandyTools.getAnchor("user", metaImage.getOwner(), metaImage.getOwner()).asString());
        score.setHTML("<b>Score: </b>" + metaImage.getScore());
        description.setHTML("<h1>Description</h1><br>"
                + HandyTools.formatHtml(metaImage.getDescription()));
        tags.setHTML(HandyTools.buildTagHTML(metaImage.getTags()));
        date.setHTML("<b>Completed: </b>" + metaImage.getDate());
        link.setHTML("<b>Link: </b>" + HandyTools.getAnchor("picture", Integer.toString(id), "Click to copy"));
        fetchMission(metaImage.getMission().getId());
    }

    private void fetchMission(int id) {
        missionService.getMission(id, true, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading the mission information."));
            }

            @Override
            public void onSuccess(Mission m) {
                if(m != null) {
                    mission.setHTML("<b>Mission: </b>" + HandyTools.getAnchor("mission", Integer.toString(m.getId()), m.getName()).asString());
                    category.setHTML("<b>Categories: </b>" + HandyTools.getCategoryAnchors(m.getCategories()));
                }
                loadingPanel.clear();
                setVisible(true);
            }
        });
    }

    @Override
    public Widget asWidget() {
        id = Integer.parseInt(parameter);
        init();
        onLoad();
        return main;
    }
}
