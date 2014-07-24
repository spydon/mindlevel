package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rating")
public interface RatingService extends RemoteService {
    int getVoteValue(String username, int pictureId) throws IllegalArgumentException;
    void setVoteValue(String token, int pictureId, boolean isUpVote) throws IllegalArgumentException;
    int getScore(int id) throws IllegalArgumentException;
    int getVoteNumber(int id, boolean countUpVotes, boolean countDownVotes) throws IllegalArgumentException;
}
