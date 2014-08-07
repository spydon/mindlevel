package net.mindlevel.client.services;

import net.mindlevel.shared.Quote;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("quote")
public interface QuoteService extends RemoteService {
    Quote getQuote() throws IllegalArgumentException;
    Quote getNotFound() throws IllegalArgumentException;
}
