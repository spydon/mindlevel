package net.mindlevel.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

/**
 * This is the class used to upload pictures.
 */
public class UploadServiceImpl extends UploadAction {

    private static final long serialVersionUID = 1L;
    private static final int PICTURE_MAXWIDTH = 800;
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
                String hashName = generateHash();
                File file = new File(path + hashName + extension);
                try {
                    //Write the image to uncompressed file
                    item.write(file);

                    // Save a list with the received files
                    receivedFiles.put(item.getFieldName(), file);
                    receivedContentTypes.put(item.getFieldName(), item.getContentType());

                    // Decrease the size of the picture if needed
                    BufferedImage scaledImage = scaleImage(file, PICTURE_MAXWIDTH);
                    File scaledFile = new File(path + hashName + "_scaled" + extension);
                    if(ImageIO.write(scaledImage, extension.substring(1), scaledFile)) {
                        // Send a message with the filename to the client.
                        response = scaledFile.getName();
                    } else {
                        response = file.getName();
                    }
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

    private BufferedImage scaleImage(File image, int scaledWidth) throws IOException {
        BufferedImage sourceImage = ImageIO.read(image);
        if(sourceImage.getWidth() > scaledWidth) {
            float scale = scaledWidth / (float) sourceImage.getWidth();
            int scaledHeight = (int) (sourceImage.getHeight() * scale);
            Image scaledImage = sourceImage.getScaledInstance(
               scaledWidth,
               scaledHeight,
               Image.SCALE_AREA_AVERAGING
            );

            BufferedImage bufferedImage = new BufferedImage(
               scaledImage.getWidth(null),
               scaledImage.getHeight(null),
               BufferedImage.TYPE_INT_RGB
            );
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();
            return bufferedImage;
        } else {
            return sourceImage;
        }

     }

    private String generateHash() {
        return UUID.randomUUID().toString();
    }
}