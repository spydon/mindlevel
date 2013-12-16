package net.mindlevel.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

/**
 * This is an example of how to use UploadAction class.
 */
public class UploadServiceImpl extends UploadAction {

//    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
    /**
     * Maintain a list with received files and their content types.
     */
    Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

    /**
     * Override executeAction to save the received files in a custom place
     * and delete this items from session.
     */
    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = "";
        for (FileItem item : sessionFiles) {
            if (false == item.isFormField() && item.getContentType().substring(0,5).equals("image")) {
                String path = "pictures/";
                //String fileName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
                String fileName = item.getName();
                String extension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
                File file = new File(path + generateHash() + extension);
                try {
                    while(file.exists())
                        file = new File(path + generateHash() + extension);
                    item.write(file);
                    // Save a list with the received files
                    receivedFiles.put(item.getFieldName(), file);
                    receivedContentTypes.put(item.getFieldName(), item.getContentType());
                    /// Send a customized message to the client.
                    response = file.getName();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("Faulty file type");
            }
        }

        /// Remove files from session because we have a copy of them
//        removeSessionFileItems(request);

        /// Send your customized message to the client.
        return response;
    }

    /**
     * Get the content of an uploaded file.
     */
    @Override
    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fieldName = request.getParameter(UConsts.PARAM_SHOW);
        File f = receivedFiles.get(fieldName);
        if (f != null) {
            response.setContentType(receivedContentTypes.get(fieldName));
            FileInputStream is = new FileInputStream(f);
            copyFromInputStreamToOutputStream(is, response.getOutputStream());
        } else {
            renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
        }
    }

    private String generateHash() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}