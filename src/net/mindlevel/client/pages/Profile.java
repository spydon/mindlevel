package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Profile {
	private RootPanel appArea;
	private String userId;
	private boolean canChange;
	private User user;
	
	/**
	 * Create a remote service proxy to talk to the server-side user
	 * service.
	 */
	private final UserServiceAsync userService = GWT
			.create(UserService.class);

	public Profile(RootPanel appArea, String userId, boolean canChange) {
		this.appArea = appArea;
		this.userId = userId;
		this.canChange = canChange;
		init();
	}
	
	private void init() {
		final VerticalPanel profilePanel = new VerticalPanel();
		profilePanel.setStylePrimaryName("cardpanel");
		userService.getUser(userId, new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
				appArea.clear();
				new Home(appArea);
			}

			public void onSuccess(User userinfo) {
				user = userinfo;
				profilePanel.add(new Label("Profile " + userId));
				profilePanel.add(new Label("Token " + Mindlevel.user.getToken()));
				profilePanel.add(new Label(user.toString()));
				appArea.add(profilePanel);
			}
		});
	}
}
