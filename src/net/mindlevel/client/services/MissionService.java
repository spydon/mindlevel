package net.mindlevel.client.services;

import java.util.List;

import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.Mission;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("mission")
public interface MissionService extends RemoteService {
    List<Mission> getMissions(int start, int end, boolean adult, boolean validated) throws IllegalArgumentException;
    List<Mission> getMissions(int start, int end, Constraint constraint) throws IllegalArgumentException;
    int getMissionCount(boolean adult, boolean validated) throws IllegalArgumentException;
    Mission getMission(int id, boolean validated) throws IllegalArgumentException;
    void uploadMission(Mission mission, String token) throws IllegalArgumentException;
    void validateMission(int missionId, String username, String token) throws IllegalArgumentException;
}
