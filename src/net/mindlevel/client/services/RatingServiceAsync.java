package net.mindlevel.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>TokenService</code>.
 */
public interface RatingServiceAsync {
    void getVoteValue(String username, int pictureId, AsyncCallback<Integer> callback)
            throws IllegalArgumentException;
    void setVoteValue(String username, int pictureId, boolean isUpVote, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
    void getScore(int id, AsyncCallback<Double> callback)
            throws IllegalArgumentException;
    void getVoteNumber(int id, boolean countUpVotes, boolean countDownVotes, AsyncCallback<Integer> callback)
            throws IllegalArgumentException;
}
