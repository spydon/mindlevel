package net.mindlevel.client.services;

import java.util.List;

import net.mindlevel.shared.Category;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("category")
public interface CategoryService extends RemoteService {
    List<Category> getCategories();
    //List<String> getCategories(int userId);
}