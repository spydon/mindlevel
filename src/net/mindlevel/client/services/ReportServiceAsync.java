package net.mindlevel.client.services;

import net.mindlevel.shared.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ReportService</code>.
 */
public interface ReportServiceAsync {
    void addReport(Report report, String token, AsyncCallback<Void> callback);
}