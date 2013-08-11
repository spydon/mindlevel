package net.mindlevel.client.services;

import java.util.ArrayList;
import java.util.List;

import net.mindlevel.shared.Mission;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface MissionServiceAsync {
	void getMissions(int start, int end, boolean validated, AsyncCallback<List<Mission>> callback)
			throws IllegalArgumentException;
	void getMissionCount(boolean validated, AsyncCallback<Integer> callback)
			throws IllegalArgumentException;
	void getMission(int missionId, boolean validated, AsyncCallback<Mission> asyncCallback);
	void suggestMission(Mission mission, AsyncCallback<Void> asyncCallback);
	void uploadMission(Mission mission, String token, AsyncCallback<Void> asyncCallback);
	void getCategories(AsyncCallback<ArrayList<String>> asyncCallback);

}
