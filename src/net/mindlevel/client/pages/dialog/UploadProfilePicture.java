package net.mindlevel.client.pages.dialog;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.widgets.LoadingElement;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadProfilePicture {
    // A panel where the thumbnails of uploaded images will be shown
    private FlowPanel panelImages;
    private final SingleUploader defaultUploader = new SingleUploader();
    private FlexTable t;
    private String filename = "";
    private final HTML header;
    private final DecoratedPopupPanel popup;
    private final VerticalPanel panel;


    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    public UploadProfilePicture() {
        popup = new DecoratedPopupPanel(false);
        panel = new VerticalPanel();
        header = new HTML("<h1>Upload Profile Picture</h1>");
        init();
    }

    private void init() {
        // Attach the image viewer to the document
        panelImages = new FlowPanel();
        panel.add(panelImages);
        String[] validExtensions = {"jpg", "jpeg", "png", "gif"};
        defaultUploader.setValidExtensions(validExtensions);
        // Initiate the FlexTable
        t = new FlexTable();

        panel.add(header);

        // Create a new uploader panel and attach it to the document
        panel.add(defaultUploader);
        FlexTable metaData = getMetaDataPanel();
        panel.add(metaData);

        // Add a finish handler which will load the image once the upload finishes
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

        popup.add(panel);
        popup.center();
        popup.show();
    }

    private FlexTable getMetaDataPanel() {
        Label adultL = new Label("18+ only?");
        final CheckBox adultCB = new CheckBox();
        Button uploadB = new Button("Upload");
        uploadB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!filename.equals("")) {
                    userService.setProfilePicture(filename, adultCB.getValue(), Mindlevel.user.getUsername(), Mindlevel.user.getToken(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                        }

                        @Override
                        public void onSuccess(Void msg) {
                            Mindlevel.user.setPicture(filename);
                            popup.hide();
                            HandyTools.showDialogBox("Success", new HTML("<h1>Successfully updated your profile picture</h1>"));
                            History.fireCurrentHistoryState();
                        }
                    });
                } else {
                    HandyTools.showDialogBox("Error", new HTML("You probably forgot to press 'send' on the picture"));
                }
            }
        });
        Button closeB = new Button("Close");
        closeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.hide();
            }
        });
        t.setWidget(0, 0, adultL);
        t.setWidget(0, 1, adultCB);
        t.setWidget(1, 0, uploadB);
        t.setWidget(1, 1, closeB);
        return t;
    }

    private final LoadingElement l = new LoadingElement();
    // Load the image in the document and in the case of success attach it to the viewer
    private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                new PreloadedImage(uploader.getServerInfo().getFileUrl(), showImage);
                panel.remove(defaultUploader);
                panel.insert(l, 2);
                // The server sends useful information to the client by default
//                UploadedInfo info = uploader.getServerInfo();

                filename = uploader.getServerMessage().getMessage();
            }
        }
    };

    // Attach an image to the pictures viewer
    private final OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
        @Override
        public void onLoad(PreloadedImage image) {
            l.removeFromParent();
            image.setWidth("150px");
            panelImages.add(image);
            popup.center();
        }
    };

}
