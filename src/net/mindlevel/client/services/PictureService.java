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
    void validate(int pictureId, String token) throws IllegalArgumentException;
    void deleteTags(int pictureId, String token) throws IllegalArgumentException;
    void delete(int pictureId, String token) throws IllegalArgumentException;
}
