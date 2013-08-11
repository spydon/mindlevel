package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.Mission;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("oracle")
public interface OracleService extends RemoteService {
	ArrayList<String> getUsers() throws IllegalArgumentException;
	ArrayList<Mission> getMissions() throws IllegalArgumentException;
}
