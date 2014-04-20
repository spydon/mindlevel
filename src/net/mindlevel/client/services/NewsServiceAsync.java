package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.News;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CommentService</code>.
 */
public interface NewsServiceAsync {
    void getNews(int number, AsyncCallback<ArrayList<News>> callback)
        throws IllegalArgumentException;
}