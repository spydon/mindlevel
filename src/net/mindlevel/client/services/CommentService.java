package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.Comment;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("comment")
public interface CommentService extends RemoteService {
    ArrayList<Comment> getComments(int threadId) throws IllegalArgumentException;

    Integer addComment(Comment comment);
}