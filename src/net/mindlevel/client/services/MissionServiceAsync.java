package net.mindlevel.client.services;

import java.util.List;

import net.mindlevel.shared.Mission;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface MissionServiceAsync {
    void getMissions(int start, int end, boolean adult, boolean validated, AsyncCallback<List<Mission>> callback)
            throws IllegalArgumentException;
    void getMissionCount(boolean adult, boolean validated, AsyncCallback<Integer> callback)
            throws IllegalArgumentException;
    void getMission(int missionId, boolean validated, AsyncCallback<Mission> asyncCallback);
    void uploadMission(Mission mission, String token, AsyncCallback<Void> asyncCallback);
    void validateMission(int missionId, String username, String token, AsyncCallback<Void> asyncCallback);
}
