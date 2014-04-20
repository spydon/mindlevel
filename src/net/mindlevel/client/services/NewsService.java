package net.mindlevel.client.services;

import java.util.ArrayList;

import net.mindlevel.shared.News;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("news")
public interface NewsService extends RemoteService {
    ArrayList<News> getNews(int number) throws IllegalArgumentException;
}
