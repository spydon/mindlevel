package net.mindlevel.client.pages;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.widgets.CommentSection;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.client.widgets.NotFoundElement;
import net.mindlevel.client.widgets.VotingSection;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Picture {
    private final RootPanel appArea;
    private final Image image = new Image();
    private final Image leftArrow = new Image(Mindlevel.PATH + "images/icons/left.svg");
    private final Image rightArrow = new Image(Mindlevel.PATH + "images/icons/right.svg");
    private int id = 0;
    private int realId = 1;
    private int imageCount = Integer.MAX_VALUE;
    private int nativeWidth, nativeHeight;
    private boolean validated = true;
    private boolean notFound = false;
    private final HTML title, description, location, uploader, tags, date, mission, category, link, score;
    private final VerticalPanel backPanel = new VerticalPanel();
    private final VerticalPanel ratingPanel = new VerticalPanel();
    private final VerticalPanel commentPanel = new VerticalPanel();
    private HorizontalPanel metaPanel;
    private HorizontalPanel picturePanel;
    private final Button validate = new Button("Validate");
    private final Button delete = new Button("Delete");

    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    /**
     * Create a remote service proxy to talk to the server-side mission
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    /**
     * Create a remote service proxy to talk to the server-side metaupload
     * service.
     */
    final MetaUploadServiceAsync metaUploadService = GWT
            .create(MetaUploadService.class);

    public Picture(RootPanel appArea, int id, boolean validated) {
        HandyTools.setLoading(true);
        this.appArea = appArea;
        this.id = id;
        this.validated = validated;
        title = new HTML();
        location = new HTML();
        uploader = new HTML();
        description = new HTML();
        mission = new HTML();
        category = new HTML();
        tags = new HTML();
        score = new HTML();
        link = new HTML();
        date = new HTML();
        image.addStyleName("mission-picture");
        title.addStyleName("picture-title");
        location.addStyleName("picture-info");
        tags.addStyleName("picture-info");
        uploader.addStyleName("picture-info");
        category.addStyleName("picture-info");
        mission.addStyleName("picture-info");
        score.addStyleName("score-info");
        link.addStyleName("picture-info");
        date.addStyleName("picture-info");
        Window.enableScrolling(true);
        init();
    }

    private void init() {
        Window.addResizeHandler(new ResizeHandler() {
            Timer resizeTimer = new Timer() {
                @Override
                public void run() {
                    adjustImageSize();
                }
              };
            @Override
            public void onResize(ResizeEvent event) {
                resizeTimer.cancel();
                resizeTimer.schedule(250);
            }
        });

        VerticalPanel containerPanel = new VerticalPanel();
        picturePanel = new HorizontalPanel();
        picturePanel.setStylePrimaryName("picture-panel");
        image.setStylePrimaryName("picture");
        leftArrow.setStylePrimaryName("arrow-left");
        rightArrow.setStylePrimaryName("arrow-right");

        image.addClickHandler(new ClickHandler() {
            /**
             * Fired when the user clicks on the sendButton.
             */
            @Override
            public void onClick(ClickEvent event) {
                randomImage();
            }
        });

        picturePanel.add(leftArrow);
        picturePanel.add(image);
        picturePanel.add(rightArrow);
        containerPanel.add(picturePanel);
        leftArrow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                prevImage();
            }
        });
        rightArrow.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                nextImage();
            }
        });
        NavigationHandler imageHandler = new NavigationHandler();
        if(Mindlevel.navigationHandlerRegistration != null) {
            Mindlevel.navigationHandlerRegistration.removeHandler();
        }
        Mindlevel.navigationHandlerRegistration = RootPanel.get().addDomHandler(imageHandler, KeyUpEvent.getType());
        metaPanel = new HorizontalPanel();
        metaPanel.addStyleName("meta-panel");
        VerticalPanel infoPanel = new VerticalPanel();

        SimplePanel descriptionContainer = new SimplePanel();
        descriptionContainer.addStyleName("picture-description");
        descriptionContainer.add(description);

        infoPanel.addStyleName("info-panel");
        infoPanel.add(uploader);
        infoPanel.add(mission);
        infoPanel.add(category);
        infoPanel.add(tags);
        infoPanel.add(link);
        infoPanel.add(date);
        metaPanel.add(infoPanel);
        metaPanel.add(descriptionContainer);

        if(UserTools.isLoggedIn()) {
            if(UserTools.isModerator()) {
                validate.addStyleName("center-button");
                if(!validated) {
                    infoPanel.add(validate);
                }
            }
            if(UserTools.isAdmin()) {
                delete.addStyleName("center-button");
                infoPanel.add(delete);
            }
        }

        backPanel.setVisible(false);
        backPanel.add(title);
        backPanel.add(containerPanel);
        if(validated) {
            backPanel.add(ratingPanel);
        }
        backPanel.add(metaPanel);
        backPanel.add(commentPanel);
        appArea.add(backPanel);
        loadImage(id, false);
    }

    class NavigationHandler implements KeyUpHandler {
        /**
         * Fired when the user types.
         */
        @Override
        public void onKeyUp(KeyUpEvent event) {
            String token = History.getToken();
            boolean isPicture = token.contains("picture") && !token.contains("login") && !token.contains("register");

            String tagName = ((Element) event.getNativeEvent().getEventTarget().cast()).getTagName();
            boolean isTextAreaFocused = tagName.equals("INPUT") || tagName.equals("TEXTAREA");
            if(!notFound && !isTextAreaFocused && isPicture) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT && id < imageCount) {
                    nextImage();
                } else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT && id > 1) {
                    prevImage();
                } else if (event.getNativeKeyCode() == 'R') {
                    randomImage();
                } else if (event.getNativeKeyCode() == 'H') {
                    HandyTools.showDialogBox("Shortcuts",
                            new HTML("Right/Left Arrow - Browse pictures</br>R - Random picture</br>H - Show this help"));
                }
            }
        }
    }

    private void randomImage() {
        loadImage(-1, true);
    }

    private void nextImage() {
        if (id < imageCount) {
            loadImage(++id, true);
        }
    }

    private void prevImage() {
        if (id > 1) {
            loadImage(--id, true);
        }
    }

    private void loadImage(final int id, final boolean relative) {
        ratingPanel.clear();
        commentPanel.clear();
        setImageUrl(LoadingElement.loadingPath);
        pictureService.get(id, relative, UserTools.isAdult(), validated, new AsyncCallback<MetaImage>() {
            @Override
            public void onFailure(Throwable caught) {
                picturePanel.clear();
                picturePanel.add(new NotFoundElement());
                hideFields();
                notFound = true;
                backPanel.setVisible(true);
                HandyTools.setLoading(false);
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                ratingPanel.setVisible(true);
                setImageUrl(Mindlevel.PATH + "pictures/" + metaImage.getFilename());
                imageCount = metaImage.getImageCount();
                if(id == 0) {
                    setId(imageCount);
                } else if(!relative || id == -1) {
                    setId(metaImage.getRelativeId());
                }

                //Check if the left arrow is needed
                if (getId() == 1) {
                    leftArrow.addStyleName("hidden");
                } else if(leftArrow.getStyleName().contains("hidden")) {
                    leftArrow.removeStyleName("hidden");
                }

                //Check if the right arrow is needed
                if (getId() == imageCount) {
                    rightArrow.addStyleName("hidden");
                } else if(rightArrow.getStyleName().contains("hidden")) {
                    rightArrow.removeStyleName("hidden");
                }

                //If it is a 'notfound' picture
                if (imageCount == 0) {
                    leftArrow.addStyleName("hidden");
                    rightArrow.addStyleName("hidden");
                    hideFields();
                }

                //If the image is validated, fetch the votevalue, else add validation button
                realId = metaImage.getId();
                History.newItem("picture=" + realId, false);
                if(UserTools.isModerator()){
                    validate.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            pictureService.validate(realId, Mindlevel.user.getToken(),
                                    new AsyncCallback<Void>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    HandyTools.showDialogBox("Success", new HTML("Great success!"));
                                    if (id > 1) {
                                        prevImage();
                                    } else if (id < imageCount-1) {
                                        nextImage();
                                    } else {
                                        randomImage();
                                    }
                                }
                            });
                        }
                    });
                }

                if(UserTools.isAdmin()) {
                    delete.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            pictureService.delete(realId, Mindlevel.user.getToken(),
                                    new AsyncCallback<Void>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    HandyTools.showDialogBox("Success", new HTML("Picture deleted!"));
                                    if (id > 1) {
                                        prevImage();
                                    } else if (id < imageCount) {
                                        nextImage();
                                    } else {
                                        randomImage();
                                    }
                               }
                            });
                        }
                    });
                }

                title.setHTML(metaImage.getTitle());
                location.setHTML("<b>Location: </b>" + metaImage.getLocation());
                uploader.setHTML("<b>Uploader: </b>" + HandyTools.getAnchor("user", metaImage.getOwner(), metaImage.getOwner()).asString());
                description.setHTML("<h1>Description</h1><br>"
                        + HandyTools.formatHtml(metaImage.getDescription()));
                tags.setHTML(HandyTools.buildTagHTML(metaImage.getTags()));
                date.setHTML("<b>Completed: </b>" + metaImage.getDate());
                if(validated) {
                    link.setHTML("<b>Link: </b>" + HandyTools.getAnchor("picture", Integer.toString(realId), "Right click to copy"));
                } else {
                    link.setHTML("<b>Link: </b><a href=#picture="+realId+"&validated=false>Right click to copy</a>");
                }
                fetchMission(metaImage.getMission().getId());
                ratingPanel.add(new VotingSection(realId));
                commentPanel.add(new CommentSection(metaImage.getThreadId()));
                backPanel.setVisible(true);
                HandyTools.setLoading(false);
            }
        });
    }

    private void setImageUrl(final String url) {
        final Image tmpImage = new Image();
        tmpImage.addLoadHandler(new LoadHandler() {

            @Override
            public void onLoad(LoadEvent event) {
                nativeWidth = tmpImage.getWidth();
                nativeHeight = tmpImage.getHeight();
                adjustImageSize();
                image.setUrl(url);
            }
        });
        //Because it needs to load the picture first and then set the ratio and size
        tmpImage.setVisible(false);
        appArea.add(tmpImage);
        tmpImage.setUrl(url);
    }

    private void adjustImageSize() {
        int width = nativeWidth;
        int height = nativeHeight;
        int arrowWidths = leftArrow.getWidth()+rightArrow.getWidth();
        int clientWidth = Window.getClientWidth();

        //If the window is under 520px but the pictures native width is too big
        if(clientWidth <= 520) {
            if(nativeWidth > clientWidth) {
                width = clientWidth-10;
                height = height*width/nativeWidth;
            }
        //If the image+arrows are too wide for the screen
        } else if(width+arrowWidths>clientWidth) {
            width = clientWidth-arrowWidths-20;
            height = height*width/nativeWidth;
        }

        image.setPixelSize(width, height);
    }

    private void hideFields() {
        metaPanel.setVisible(false);
        leftArrow.setVisible(false);
        rightArrow.setVisible(false);
        title.setVisible(false);
        score.setVisible(false);
        ratingPanel.setVisible(false);
        commentPanel.setVisible(false);
        validate.setVisible(false);
        delete.setVisible(false);
    }

    private void fetchMission(int id) {
        missionService.getMission(id, true, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading the mission information."));
            }

            @Override
            public void onSuccess(Mission m) {
                if(m != null) {
                    mission.setHTML("<b>Mission: </b>" + HandyTools.getAnchor("mission", Integer.toString(m.getId()), m.getName()).asString());
                    category.setHTML("<b>Categories: </b>" + HandyTools.getCategoryAnchors(m.getCategories()));
                }
            }
        });
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}