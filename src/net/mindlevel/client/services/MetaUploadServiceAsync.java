package net.mindlevel.client.services;

import net.mindlevel.shared.MetaImage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetaUploadServiceAsync {
	void upload(MetaImage metaImage, boolean validated, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
