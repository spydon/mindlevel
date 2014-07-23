package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.pages.dialog.SureBox;
import net.mindlevel.client.pages.dialog.Upload;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.widgets.GallerySection;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.SearchType;

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

    public MissionProfile(RootPanel appArea, Mission mission) {
        HandyTools.setLoading(true);
        this.appArea = appArea;
        this.mission = mission;
        this.missionId = mission.getId();
        this.validated = mission.isValidated();
        History.newItem("mission=" + missionId, false);
        showMission();
    }

    public MissionProfile(RootPanel appArea, int missionId, boolean validated) {
        HandyTools.setLoading(true);
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
                HandyTools.setLoading(false);
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                appArea.clear();
                new Home(appArea);
            }

            @Override
            public void onSuccess(Mission missioninfo) {
                mission = missioninfo;
                showMission();
            }
        });
    }

    private void showMission() {
        HandyTools.setLoading(false);
        appArea.clear();
        VerticalPanel missionPanel = new VerticalPanel();
        missionPanel.addStyleName("mission-panel");
        missionPanel.add(new HTML("<b>MissionID:</b> " + missionId));
        missionPanel.add(new HTML("<b>Name:</b> " + mission.getName()));
        missionPanel.add(new HTML("<b>Categories:</b> " + HandyTools.getCategoryAnchors(mission.getCategories())));
        missionPanel.add(new HTML("<b>Description:</b><br>" + HandyTools.formatHtml(mission.getDescription())));

        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.addStyleName("button-panel");
        if(UserTools.isLoggedIn() && validated) {
            Button uploadButton = new Button("Upload completed mission");
            uploadButton.addStyleName("smallmargin");
            uploadButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    new Upload(missionId, mission.getName());
                }
            });
            buttonPanel.add(uploadButton);
        }
        if(UserTools.isAdmin()) {
            if(!validated) {
                Button validateButton = new Button("Validate");
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
                                        appArea.clear();
                                        Constraint constraint = new Constraint();
                                        constraint.setValidated(false);
                                        constraint.setAdult(UserTools.isAdult());
                                        new Missions(appArea, constraint);
                                    }
                                });
                    }
                });
                buttonPanel.add(validateButton);
            }

            final SureBox sureBox = new SureBox();
            final ClickHandler yesHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent arg0) {
                    sureBox.hide();
                    missionService.deleteMission(
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
                                    HandyTools.showDialogBox("Success!", new HTML("\"" + mission.getName() + "\" is now deleted!"));
                                    appArea.clear();
                                    Constraint constraint = new Constraint();
                                    constraint.setAdult(UserTools.isAdult());
                                    constraint.setValidated(validated);
                                    new Missions(appArea, constraint);
                                }
                            });

                }
            };

            final ClickHandler noHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent arg0) {
                    sureBox.hide();
                }
            };

            Button deleteButton = new Button("Delete");

            deleteButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    sureBox.addYesHandler(yesHandler);
                    sureBox.addNoHandler(noHandler);
                    sureBox.show();
                }

            });
            buttonPanel.add(deleteButton);
        }

        final Button galleryButton = new Button("Show finished \"" + mission.getName() + "\"");
        galleryButton.addStyleName("profile-gallery-button");
        galleryButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                Constraint constraint = new Constraint();
                constraint.setMissionId(missionId);
                constraint.setValidated(true);
                constraint.setType(SearchType.PICTURE);
                constraint.setToken(UserTools.getToken());
                constraint.setAdult(UserTools.isAdult());
                GallerySection gallerySection = new GallerySection(constraint);
                HTML galleryHeader = new HTML("<h1>Finished \"" + mission.getName() + "\"</h>");
                galleryHeader.addStyleName("profile-gallery-header");
                appArea.add(galleryHeader);
                appArea.add(gallerySection);
                galleryButton.removeFromParent();
            }
        });
        buttonPanel.add(galleryButton);
        missionPanel.add(buttonPanel);

        appArea.add(missionPanel);
    }
}
