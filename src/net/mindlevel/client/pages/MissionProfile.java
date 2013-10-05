package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MissionProfile {
	private RootPanel appArea;
	private int missionId;
	private Mission mission;
	private boolean validated = true;
	
	/**
	 * Create a remote service proxy to talk to the server-side user
	 * service.
	 */
	private final MissionServiceAsync missionService = GWT
			.create(MissionService.class);
	
	public MissionProfile(RootPanel appArea, int missionId, boolean validated) {
		this.appArea = appArea;
		this.missionId = missionId;
		this.validated = validated;
		init();
	}
	
	private void init() {
		missionService.getMission(missionId, validated, new AsyncCallback<Mission>() {
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
				appArea.clear();
				new Home(appArea);
			}

			public void onSuccess(Mission missioninfo) {
				appArea.clear();
				mission = missioninfo;
				showMission();
			}
		});
	}
	
	private void showMission() {
		VerticalPanel missionPanel = new VerticalPanel();
		missionPanel.setStylePrimaryName("cardpanel");
		missionPanel.add(new Label("MissionID " + missionId));
		missionPanel.add(new Label("Name " + mission.getName()));
		missionPanel.add(new Label("Category " + mission.getCategory()));	
		missionPanel.add(new Label("Description " + mission.getDescription()));
		if(HandyTools.isLoggedIn() && validated) {
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
		if(Mindlevel.user.isAdmin() && !validated) {
			Button validateButton = new Button("Validate");
			validateButton.addStyleName("smallmargin");
			validateButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					missionService.uploadMission(mission, Mindlevel.user.getToken(), new AsyncCallback<Void>() {
		
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
