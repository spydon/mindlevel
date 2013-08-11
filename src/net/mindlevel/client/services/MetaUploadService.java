package net.mindlevel.client.services;

import net.mindlevel.shared.MetaImage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("metaUpload")
public interface MetaUploadService extends RemoteService {
	String upload(MetaImage metaImage, boolean validated) throws IllegalArgumentException;
}
