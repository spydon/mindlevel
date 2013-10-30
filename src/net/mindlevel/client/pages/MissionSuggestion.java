package net.mindlevel.client.pages;


import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.TokenService;
import net.mindlevel.client.services.TokenServiceAsync;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MissionSuggestion {
	private FlexTable t;
	private final HTML header;
	private final DecoratedPopupPanel popup;
	private final VerticalPanel panel;

	private final MissionServiceAsync missionService = GWT
			.create(MissionService.class);

	private final static TokenServiceAsync tokenService = GWT
			.create(TokenService.class);

	public MissionSuggestion() {
		popup = new DecoratedPopupPanel(false);
		panel = new VerticalPanel();
		header = new HTML("<h1>Suggest Mission</h1>");
		init();
	}

	private void init() {
		// Initiate the FlexTable
		t = new FlexTable();

		panel.add(header);

		// Create a new uploader panel and attach it to the document
		FlexTable metaData = getMetaDataPanel();
		panel.add(metaData);

		popup.add(panel);
		popup.center();
		popup.show();
	}

	private FlexTable getMetaDataPanel() {
		Label titleL = new Label("Title");
		final TextBox titleTB = new TextBox();
		Label categoryL = new Label("Category");
		final ListBox categoryLB = new ListBox();
		getCategories(categoryLB);
		Label descriptionL = new Label("Description");
		final TextArea descriptionTA = new TextArea();
		Label adultL = new Label("18+ only?");
		final CheckBox adultCB = new CheckBox();
		Button uploadB = new Button("Upload for validation");
		uploadB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Mission mission = new Mission(titleTB.getText(), categoryLB.getItemText(categoryLB.getSelectedIndex()), descriptionTA.getText(), Mindlevel.user.getUsername(), adultCB.getValue());
				if(FieldVerifier.isValidMission(mission)) {
					missionUpload(mission);
				} else {
					HandyTools.showDialogBox("Error", new HTML("You probably forgot to fill out one of the fields!"));
				}

			}
		});
		Button closeB = new Button("Close");
		closeB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
		t.setWidget(0, 0, titleL);
		t.setWidget(0, 1, titleTB);
		t.setWidget(1, 0, descriptionL);
		t.setWidget(1, 1, descriptionTA);
		t.setWidget(2, 0, categoryL);
		t.setWidget(2, 1, categoryLB);
		t.setWidget(3, 0, adultL);
		t.setWidget(3, 1, adultCB);
		t.setWidget(4, 1, uploadB);
		t.setWidget(5, 1, closeB);
		return t;
	}

	private void missionUpload(final Mission mission) {
		// Then, we send the input to the server.
		tokenService.validateToken(Mindlevel.user.getToken(), new AsyncCallback<Boolean>() {
		@Override
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML("You don't seem to be logged in properly..."));
			}

			@Override
			public void onSuccess(Boolean result) {
				missionService.suggestMission(mission, new AsyncCallback<Void>() {
					@Override
                    public void onFailure(Throwable caught) {
						HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
					}

					@Override
                    public void onSuccess(Void noreturn) {
						popup.hide();
						HandyTools.showDialogBox("Success", new HTML("<h1>Successfully suggested a mission.</h1>"));
					}
				});
			}
		});
	}

	private void getCategories(final ListBox categoryLB) {
		missionService.getCategories(new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML("Could not load categories, try reloading the page."));
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				for(String category : result)
					categoryLB.addItem(category);
			}
		});
	}
}
