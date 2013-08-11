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
		userService.getUser(userId, new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
				appArea.clear();
				new Home(appArea);
				// Show the RPC error message to the user
//				serverResponseLabel.addStyleName("serverResponseLabelError");
				// serverResponseLabel.setHTML(SERVER_ERROR);
//				serverResponseLabel.setHTML(caught.getMessage());
			}

			public void onSuccess(User userinfo) {
				user = userinfo;
				appArea.add(new Label("Profile " + userId));
				appArea.add(new Label("Token " + Mindlevel.user.getToken()));
				appArea.add(new Label(user.toString()));
			}
		});
	}

	public boolean canChange() {
		return canChange;
	}
}
