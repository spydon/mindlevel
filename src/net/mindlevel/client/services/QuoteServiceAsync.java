package net.mindlevel.client.services;

import net.mindlevel.shared.Quote;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>QuoteService</code>.
 */
public interface QuoteServiceAsync {
    void getQuote(AsyncCallback<Quote> callback)
        throws IllegalArgumentException;
}