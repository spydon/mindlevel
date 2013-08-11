package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.Mission;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OracleServiceAsync {
	void getUsers(AsyncCallback<ArrayList<String>> callback)
			throws IllegalArgumentException;
	void getMissions(AsyncCallback<ArrayList<Mission>> callback)
			throws IllegalArgumentException;
}
