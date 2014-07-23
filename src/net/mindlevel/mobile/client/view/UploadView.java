package net.mindlevel.mobile.client.view;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MTextArea;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.input.checkbox.MCheckBox;

public class UploadView extends MPage {
    private int id = -1;
    private String filename = "";
    private final VerticalPanel main;
    private final HTML missionTitle;
    private final LoadingElement loading;
    private final SimplePanel loadingPanel, picturePanel;
    private final MTextBox title, location;
    private final MCheckBox adult;
    private final MTextArea description;
    private final VerticalPanel formPanel;

    private final MultiWordSuggestOracle userOracle = new MultiWordSuggestOracle();
    private boolean educatedUserOracle = false;
    private final ArrayList<TagLine> tagLines = new ArrayList<TagLine>();

    private final SingleUploader defaultUploader = new SingleUploader();

    private final MetaUploadServiceAsync metaUploadService = GWT
            .create(MetaUploadService.class);

    private final UserServiceAsync userService = GWT
            .create(UserService.class);


    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public UploadView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");

        String[] validExtensions = {"jpg", "jpeg", "png", "gif"};
        defaultUploader.setValidExtensions(validExtensions);
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

        missionTitle = new HTML("");
        loading = new LoadingElement();
        loadingPanel = new SimplePanel();
        picturePanel = new SimplePanel();

        title = new MTextBox();
        location = new MTextBox();
        adult = new MCheckBox();
        adult.setValue(false);
        description = new MTextArea();

        tagLines.add(new TagLine());

        formPanel = new VerticalPanel();
        FormEntry uploadE = new FormEntry("Picture", defaultUploader);
        FormEntry titleE = new FormEntry("Title", title);
        FormEntry locationE = new FormEntry("Location", location);
        FormEntry adultE = new FormEntry("Adult?", adult);
        FormEntry describeE = new FormEntry("Description", description);

        Button uploadB = new Button("Upload for validation");
        uploadB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                MetaImage metaImage = new MetaImage(filename, title.getText(),
                        location.getText(), new Mission(id), UserTools.getUsername(), description.getText(),
                        getTags(), adult.getValue());
                metaImage.setToken(UserTools.getToken());
                String reason = FieldVerifier.isValidMetaImage(metaImage);
                if(reason.equals("")) {
                    metaUpload(metaImage);
                } else {
                    HandyTools.showDialogBox("Error", new HTML(reason));
                }
            }
        });
        Button helpB = new Button("Help");
        helpB.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                HandyTools.showDialogBox("Help", new HTML("Remember to press send on the picture before pressing upload.</br>The picture can be up to 6MB.</br>It's not necessary to tag yourself."));
            }
        });

        formPanel.add(uploadE);
        formPanel.add(titleE);
        formPanel.add(locationE);
        formPanel.add(adultE);
        formPanel.add(describeE);
        formPanel.add(tagLines.get(0));

        loadingPanel.add(loading);

        main.add(new HTML("<h1>Upload</h1>"));
        main.add(loadingPanel);
        main.add(picturePanel);
        main.add(missionTitle);
        main.add(formPanel);
        main.add(uploadB);
        main.add(helpB);
    }

    public void init() {
        if(!educatedUserOracle) {
            educateOracle();
        }
        missionTitle.setHTML("");
        loadingPanel.setWidget(loading);
        missionService.getMission(id, true, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(final Mission mission) {
                loadingPanel.clear();
                missionTitle.setHTML("<h2>" + mission.getName() + "</h2>");
            }
        });
    }

    private void metaUpload(final MetaImage metaImage) {
        // Then, we send the input to the server.
        metaUploadService.upload(metaImage, false, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(String msg) {
                HandyTools.showDialogBox("Success", new HTML("<h3>"+msg+"</h3>"));
            }
        });
    }

    // Load the image in the document and in the case of success attach it to the viewer
    private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                // You can send any customized message and parse it
                filename = uploader.getServerMessage().getMessage();

                new PreloadedImage(uploader.getServerInfo().getFileUrl(), showImage);
                picturePanel.setWidget(new LoadingElement());
            }
        }
    };

    // Attach an image to the pictures viewer
    private final OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
        @Override
        public void onLoad(PreloadedImage image) {
            image.setWidth("150px");
            picturePanel.setWidget(image);
        }
    };

    private HashSet<String> getTags() {
        HashSet<String> tags = new HashSet<String>();
        for(int x = 0; x<tagLines.size(); x++) {
            String text = tagLines.get(x).getTag();
            if(!text.equals("")) {
                tags.add(text);
            }
        }
        return tags;
    }

    protected class TagLine extends FormEntry {
        private final HorizontalPanel main;
        final SuggestBox tag = new SuggestBox(userOracle);
        final Button button = new Button();

        public TagLine() {
            tag.addStyleName("m-tagline");
            button.addStyleName("m-add-button");

            button.addTapHandler(new TapHandler() {
                @Override
                public void onTap(TapEvent event) {
                    if(tag.isEnabled()) {
                        addLine();
                    } else {
                        removeLine();
                    }
                }
            });

            tag.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

                @Override
                public void onSelection(SelectionEvent<Suggestion> arg0) {
                    addLine();
                }
            });

            main = new HorizontalPanel();
            main.add(tag);
            main.add(button);
            setWidget("Tag User", main);
        }

        public String getTag() {
            return tag.getText();
        }

        private void addLine() {
            if(tag.getText().equals("")) {
                tag.addStyleName("error-background");
            } else {
                tag.removeStyleName("error-background");
                tag.setEnabled(false);
                button.removeStyleName("m-add-button");
                button.addStyleName("m-remove-button");
                TagLine tagLine = new TagLine();
                tagLines.add(tagLine);
                formPanel.add(tagLine);
            }
        }

        private void removeLine() {
            if(tagLines.size() == 1) {
                tag.removeStyleName("error-background");
                tag.setEnabled(true);
                tag.setText("");
                button.removeStyleName("m-remove-button");
                button.addStyleName("m-add-button");
            } else {
                tagLines.remove(this);
                removeFromParent();
            }
        }
    }

    private void educateOracle() {
        userService.getUsers(new AsyncCallback<List<User>>() {
            @Override
            public void onFailure(Throwable caught) {
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

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setId(int id) {
        this.id = id;
        init();
    }

    @Override
    public void setId(String id) {
        setId(Integer.parseInt(id));
    }
}