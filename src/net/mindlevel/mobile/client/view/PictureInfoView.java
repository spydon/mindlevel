package net.mindlevel.mobile.client.view;

import java.util.HashSet;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;


public class PictureInfoView extends MPage {
    protected RootFlexPanel main;
    private final boolean initialized = false;
    private String username = "";

    /**
     * Create a remote service proxy to talk to the server-side mission
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);


    public PictureInfoView() {
        main = new RootFlexPanel();
    }

    private void init() {

    }

    private void initInfo(MetaImage metaImage) {
        //location.setHTML("<b>Location: </b>" + metaImage.getLocation());
        //owner.setHTML("<b>Owner: </b>" + HandyTools.getAnchor("user", metaImage.getOwner(), metaImage.getOwner()));
        //description.setHTML("<h1>Description</h1><br>"
        //        + HandyTools.formatHtml(metaImage.getDescription()));
        //tags.setHTML(buildTagHTML(metaImage.getTags()));
        //date.setHTML("<b>Creation date: </b>" + metaImage.getDate());
        //link.setHTML("<b>Link: </b>" + HandyTools.getAnchor("picture", Integer.toString(realId), "Right click to copy"));
        //fetchMission(metaImage.getMission().getId());
    }

    private String buildTagHTML(HashSet<String> tags) {
        String separator = ",&nbsp;";
        String tagHtml = "<b>Tags: </b>";
        if(tags!=null && !tags.isEmpty()) {
            for(String tag : tags) {
                tagHtml = tagHtml.concat(HandyTools.getAnchor("user", tag, tag));
                tagHtml = tagHtml.concat(separator);
            }
            tagHtml = tagHtml.substring(0, tagHtml.length()-separator.length());
        }
        return tagHtml;
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
//                    mission.setHTML("<b>Mission: </b>" + HandyTools.getAnchor("mission", Integer.toString(m.getId()), m.getName()));
//                    category.setHTML("<b>Categories: </b>" + HandyTools.getCategoryAnchors(m.getCategories()));
                }
            }
        });
    }

    @Override
    public void setId(int id) {
        //Not reachable
    }

    @Override
    public void setId(String id) {
        username = id;
    }

    @Override
    public Widget asWidget() {
        init();
        onLoad();
        return main;
    }
}
