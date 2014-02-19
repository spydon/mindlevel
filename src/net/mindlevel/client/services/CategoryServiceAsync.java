package net.mindlevel.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>UserService</code>.
 */
public interface CategoryServiceAsync {
    void getCategories(AsyncCallback<List<String>> asyncCallback);
    //void getCategories(int userId, AsyncCallback<List<String>> asyncCallback);
}
