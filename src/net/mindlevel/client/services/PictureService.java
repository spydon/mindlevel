package net.mindlevel.client.services;

import net.mindlevel.shared.MetaImage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("picture")
public interface PictureService extends RemoteService {
	MetaImage get(int id, boolean relative, boolean validated) throws IllegalArgumentException;
	void deleteTags(int pictureId, boolean validated, String token) throws IllegalArgumentException;
	void deletePicture(int pictureId, boolean validated, String token) throws IllegalArgumentException;
}
