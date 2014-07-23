package net.mindlevel.mobile.client.view;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.client.widgets.MissionElement;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;

public class MissionView extends MPage {
    protected VerticalPanel main;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public MissionView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    public void init() {
    }

    private void loadMission(int id) {
        main.clear();
        final LoadingElement progress = new LoadingElement();
        main.add(progress);
        missionService.getMission(id, true, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(final Mission mission) {
                main.clear();
                MissionElement missionElement = new MissionElement(mission);
                HTML missionDescription = new HTML("<h2>Description</h2>" + mission.getDescription());
                Button uploadButton = new Button("Complete mission");
                uploadButton.addTapHandler(new TapHandler() {
                    @Override
                    public void onTap(TapEvent event) {
                        if(UserTools.isLoggedIn()) {
                            History.newItem("upload=" + mission.getId());
                        } else {
                            HandyTools.notLoggedInBox();
                        }
                    }
                });

                missionDescription.addStyleName("m-mission-description");
                uploadButton.addStyleName("m-upload-button");

                main.add(missionElement);
                main.add(missionDescription);
                main.add(uploadButton);
            }
        });
    }

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setId(int id) {
        loadMission(id);
    }

    @Override
    public void setId(String id) {
        setId(Integer.parseInt(id));
    }
}