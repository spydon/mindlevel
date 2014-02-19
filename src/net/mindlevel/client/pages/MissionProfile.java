package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.pages.dialog.Upload;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MissionProfile {
    private final RootPanel appArea;
    private final int missionId;
    private Mission mission;
    private boolean validated = true;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public MissionProfile(RootPanel appArea, int missionId, boolean validated) {
        History.newItem("mission=" + missionId, false);
        this.appArea = appArea;
        this.missionId = missionId;
        this.validated = validated;
        init();
    }

    private void init() {
        missionService.getMission(missionId, validated, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                appArea.clear();
                new Home(appArea);
            }

            @Override
            public void onSuccess(Mission missioninfo) {
                appArea.clear();
                mission = missioninfo;
                showMission();
            }
        });
    }

    private void showMission() {
        VerticalPanel missionPanel = new VerticalPanel();
        missionPanel.setStylePrimaryName("profile-panel");
        missionPanel.add(new HTML("<b>MissionID</b> " + missionId));
        missionPanel.add(new HTML("<b>Name</b> " + mission.getName()));
        missionPanel.add(new HTML("<b>Categories</b> " + mission.getCategories())); //TODO: Do this differently?
        missionPanel.add(new HTML("<b>Description</b> " + mission.getDescription()));
        if(UserTools.isLoggedIn() && validated) {
            Button uploadButton = new Button("Upload completed mission");
            uploadButton.addStyleName("smallmargin");
            uploadButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    new Upload(missionId, mission.getName());
                }
            });
            missionPanel.add(uploadButton);
        }
        if(UserTools.isAdmin() && !validated) {
            Button validateButton = new Button("Validate");
            validateButton.addStyleName("smallmargin");
            validateButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    missionService.validateMission(
                            mission.getId(),
                            Mindlevel.user.getUsername(),
                            Mindlevel.user.getToken(),
                            new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    HandyTools.showDialogBox("Success!", new HTML("\"" + mission.getName() + "\" is now validated! :)"));
                                }
                            });
                }
            });
            missionPanel.add(validateButton);
        }
        appArea.add(missionPanel);
    }
}
