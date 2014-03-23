package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.Comment;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CommentService</code>.
 */
public interface CommentServiceAsync {
    void getComments(int threadId, AsyncCallback<ArrayList<Comment>> callback)
        throws IllegalArgumentException;

    void addComment(Comment comment, AsyncCallback<Integer> callback);
}