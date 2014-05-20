package net.mindlevel.client.pages.dialog;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Upload {
    // A panel where the thumbnails of uploaded images will be shown
    private FlowPanel panelImages;
    private final SingleUploader defaultUploader = new SingleUploader();
    private final MultiWordSuggestOracle userOracle = new MultiWordSuggestOracle();
    private boolean educatedUserOracle = false;
    private FlexTable t;
    private String filename = "";
    private final int missionId;
    private final HTML header;
    private final DecoratedPopupPanel popup;
    private final VerticalPanel panel;

    private final MetaUploadServiceAsync metaUploadService = GWT
            .create(MetaUploadService.class);

    private final UserServiceAsync userService = GWT
            .create(UserService.class);

    private final ArrayList<SuggestBox> tagList = new ArrayList<SuggestBox>();

    public Upload(int missionId, String missionName) {
        this.missionId = missionId;
        popup = new DecoratedPopupPanel(false);
        panel = new VerticalPanel();
        header = new HTML("<h1>Upload - " + missionName + "</h1>");
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
        Label titleL = new Label("Title");
        final TextBox titleTB = new TextBox();
        Label locationL = new Label("Location");
        final TextBox locationTB = new TextBox();
        Label descriptionL = new Label("Description");
        final TextArea descriptionTA = new TextArea();
        Label adultL = new Label("18+ only?");
        final CheckBox adultCB = new CheckBox();
        Button uploadB = new Button("Upload for validation");
        uploadB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MetaImage metaImage = new MetaImage(filename, titleTB.getText(),
                        locationTB.getText(), new Mission(missionId), Mindlevel.user.getUsername(), descriptionTA.getText(),
                        getTags(), adultCB.getValue());
                metaImage.setToken(Mindlevel.user.getToken());
                String reason = FieldVerifier.isValidMetaImage(metaImage);
                if(reason.equals("")) {
                    metaUpload(metaImage);
                } else {
                    HandyTools.showDialogBox("Error", new HTML(reason));
                }
            }
        });
        Button helpB = new Button("Help");
        helpB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                HandyTools.showDialogBox("Help", new HTML("Remember to press send on the picture before pressing upload.</br>The picture can be up to 6MB.</br>It's not necessary to tag yourself."));
            }
        });
        Button closeB = new Button("Close");
        closeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.hide();
            }
        });
        t.setWidget(0, 0, titleL);
        t.setWidget(0, 1, titleTB);
        t.setWidget(1, 0, locationL);
        t.setWidget(1, 1, locationTB);
        t.setWidget(2, 0, descriptionL);
        t.setWidget(2, 1, descriptionTA);
        createTagRow(3);
        t.setWidget(4, 0, adultL);
        t.setWidget(4, 1, adultCB);
        t.setWidget(5, 1, uploadB);
        t.setWidget(6, 1, helpB);
        t.setWidget(7, 1, closeB);
        //t.getFlexCellFormatter().setColSpan(3, 0, 2);
        return t;
    }

    private HashSet<String> getTags() {
        HashSet<String> tags = new HashSet<String>();
        for(int x = 0; x<tagList.size(); x++) {
            String text = tagList.get(x).getText();
            if(!text.equals("")) {
                tags.add(text);
            }
        }
        return tags;
    }

    private void createTagRow(final int row) {
        Label tagL = new Label("Tag ML user");
        if(!educatedUserOracle) {
            educateOracle();
        }
        final SuggestBox tagField = new SuggestBox(this.userOracle);
        final Button newTagB = new Button("+");
        final Button delTagB = new Button("-");
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(newTagB);
        buttonPanel.add(delTagB);
        t.setWidget(row, 0, tagL);
        t.setWidget(row, 1, tagField);
        t.setWidget(row, 2, buttonPanel);
        tagField.setFocus(true);
        tagList.add(tagField);
        newTagB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Cell cell = t.getCellForEvent(event);
                SuggestBox box = (SuggestBox)(t.getWidget(cell.getRowIndex(), 1));
                if(!box.getText().equals("")) {
                    t.insertRow(cell.getRowIndex()+1);
                    createTagRow(cell.getRowIndex()+1);
                    tagField.setEnabled(false);
                    box.setStyleName("error-background", false);
                } else {
                    box.setStyleName("error-background", true);
                }
            }
        });
        delTagB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Cell cell = t.getCellForEvent(event);
                tagList.remove(tagField.getText());
                if(t.getRowCount() > 8) {
                    t.removeRow(cell.getRowIndex());
                } else {
                    tagField.setEnabled(true);
                    tagField.setText("");
                }
            }
        });
        tagField.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
                    newTagB.click();
            }
        });
    }

    private void educateOracle() {
        userService.getUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<User> users) {
                userOracle.clear();
                for(User user : users) {
                    userOracle.add(user.getUsername());
                }
                educatedUserOracle = true;
            }
        });
    }

    private final LoadingElement l = new LoadingElement();

    // Load the image in the document and in the case of success attach it to the viewer
    private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                // You can send any customized message and parse it
                filename = uploader.getServerMessage().getMessage();

                new PreloadedImage(uploader.getServerInfo().getFileUrl(), showImage);
                panel.remove(defaultUploader);
                panel.insert(l, 2);
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
            if(popup.isShowing()) {
                popup.center();
            }
        }
    };

    private void metaUpload(final MetaImage metaImage) {
        // Then, we send the input to the server.
        metaUploadService.upload(metaImage, false, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(String msg) {
                popup.hide();
                HandyTools.showDialogBox("Success", new HTML("<h1>"+msg+"</h1>"));
            }
        });
    }
}
