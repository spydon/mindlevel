package net.mindlevel.client.services;

import net.mindlevel.shared.MetaImage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>PictureService</code>.
 */
public interface PictureServiceAsync {
    void get(int id, boolean relative, boolean validated, AsyncCallback<MetaImage> callback)
            throws IllegalArgumentException;
    void validate(int pictureId, String token, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
    void deleteTags(int pictureId, String token, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
    void delete(int pictureId, String token, AsyncCallback<Void> callback)
            throws IllegalArgumentException;
}