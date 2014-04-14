package net.mindlevel.client.pages;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.services.RatingService;
import net.mindlevel.client.services.RatingServiceAsync;
import net.mindlevel.client.widgets.CommentSection;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import org.cobogw.gwt.user.client.ui.Rating;

import com.google.gwt.core.client.GWT;
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
    private final String notFoundPath = "../images/notfound.jpg";
    private final Image image = new Image();
    private final Image leftArrow = new Image("../images/icons/left2.svg");
    private final Image rightArrow = new Image("../images/icons/right2.svg");
    private int id = 0;
    private int realId = 1;
    private int imageCount = Integer.MAX_VALUE;
    private int nativeWidth, nativeHeight;
    private boolean validated = true;
    private boolean notFound = false;
    private final HTML title, description, location, owner, tags, date, mission, category, link, score;
    private final VerticalPanel backPanel = new VerticalPanel();
    private final VerticalPanel ratingPanel = new VerticalPanel();
    private final VerticalPanel commentPanel = new VerticalPanel();
    private HorizontalPanel metaPanel;
    private HorizontalPanel picturePanel;
    private final Button validate = new Button("Validate");
    private final Button delete = new Button("Delete");
    private final Rating rating = new Rating(0,5,1,"../images/star.png","../images/stardeselected.png","../images/starhover.png",32,32);

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
     * Create a remote service proxy to talk to the server-side rating
     * service.
     */
    private final RatingServiceAsync ratingService = GWT
            .create(RatingService.class);

    /**
     * Create a remote service proxy to talk to the server-side metaupload
     * service.
     */
    final MetaUploadServiceAsync metaUploadService = GWT
            .create(MetaUploadService.class);

    public Picture(RootPanel appArea, int id, boolean validated) {
        this.appArea = appArea;
        this.id = id;
        this.validated = validated;
        title = new HTML();
        location = new HTML();
        owner = new HTML();
        description = new HTML();
        mission = new HTML();
        category = new HTML();
        tags = new HTML();
        score = new HTML();
        link = new HTML();
        date = new HTML();
        image.addStyleName("missionPicture");
        title.addStyleName("picture-title");
        location.addStyleName("pictureInfo");
        tags.addStyleName("pictureInfo");
        owner.addStyleName("pictureInfo");
        category.addStyleName("pictureInfo");
        mission.addStyleName("pictureInfo");
        score.addStyleName("scoreInfo");
        link.addStyleName("pictureInfo");
        date.addStyleName("pictureInfo");
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

        if(UserTools.isLoggedIn() && validated) {
            getVoteValue();
            rating.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ratingService.setVoteValue(Mindlevel.user.getToken(), realId, rating.getValue(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            HandyTools.showDialogBox("Error", new HTML("The voting doesn't work at the moment, please try again later." + caught.getMessage()));
                        }

                        @Override
                        public void onSuccess(Void result) {
                            getVoteValue();
                        }
                    });
                }
            });
        }
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
        ImageHandler imageHandler = new ImageHandler();
        if(!Mindlevel.hasKeyUpHandler) {
            RootPanel.get().addDomHandler(imageHandler, KeyUpEvent.getType());
            Mindlevel.hasKeyUpHandler = true;
        }
        metaPanel = new HorizontalPanel();
        metaPanel.addStyleName("metapanel");
        VerticalPanel infoPanel = new VerticalPanel();

        SimplePanel descriptionContainer = new SimplePanel();
        descriptionContainer.addStyleName("picture-description");
        descriptionContainer.add(description);

        infoPanel.addStyleName("infopanel");
        infoPanel.add(owner);
        infoPanel.add(mission);
        infoPanel.add(category);
        infoPanel.add(tags);
        infoPanel.add(link);
        infoPanel.add(date);
        metaPanel.add(infoPanel);
        metaPanel.add(descriptionContainer);

        //TODO: Get rid of this somehow
        VerticalPanel centerHack = new VerticalPanel();
        if(UserTools.isLoggedIn()) {
            if(validated) {
                ratingPanel.add(rating);
                centerHack.add(ratingPanel);
                centerHack.add(score);
            }
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
        backPanel.add(centerHack);
        backPanel.add(metaPanel);
        backPanel.add(commentPanel);
        appArea.add(backPanel);
        loadImage(id, false);
    }

    class ImageHandler implements KeyUpHandler {
        /**
         * Fired when the user types in the nameField.
         */
        @Override
        public void onKeyUp(KeyUpEvent event) {
            boolean isTextArea = event.getNativeEvent().getEventTarget().toString().toLowerCase().contains("textarea");
            boolean isPicture = History.getToken().contains("picture");
            if(!notFound && !isTextArea && isPicture) {
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
        HandyTools.setLoading(true);
        commentPanel.clear();
        pictureService.get(id, relative, validated, new AsyncCallback<MetaImage>() {
            @Override
            public void onFailure(Throwable caught) {
                setImageUrl(notFoundPath);
                hideFields();
                notFound = true;
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                ratingPanel.setVisible(true);
                setImageUrl("../pictures/" + metaImage.getFilename());
                imageCount = metaImage.getImageCount();
                if(id == 0)
                    setId(imageCount);
                else if(!relative || id == -1)
                    setId(metaImage.getRelativeId());

                //Check if the left arrow is needed
                if (getId() == 1)
                    leftArrow.addStyleName("hidden");
                else if(leftArrow.getStyleName().contains("hidden"))
                    leftArrow.removeStyleName("hidden");

                //Check if the right arrow is needed
                if (getId() == imageCount)
                    rightArrow.addStyleName("hidden");
                else if(rightArrow.getStyleName().contains("hidden"))
                    rightArrow.removeStyleName("hidden");

                //If it is a 'notfound' picture
                if (imageCount == 0) {
                    leftArrow.addStyleName("hidden");
                    rightArrow.addStyleName("hidden");
                    hideFields();
                }

                //If the image is validated, fetch the votevalue, else add validation button
                realId = metaImage.getId();
                History.newItem("picture=" + realId, false);
                if(UserTools.isLoggedIn() && validated) {
                    getVoteValue();
                } else if(UserTools.isModerator()){
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
                                    nextImage();
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
                                    nextImage();
                                }
                            });
                        }
                    });
                }

                title.setHTML(metaImage.getTitle());
                location.setHTML("<b>Location: </b>" + metaImage.getLocation());
                owner.setHTML("<b>Owner: </b>" + HandyTools.getAnchor("user", metaImage.getOwner(), metaImage.getOwner()));
                description.setHTML("<h1>Description</h1>"
                        + metaImage.getDescription());
                tags.setHTML(buildTagHTML(metaImage.getTags()));
                date.setHTML("<b>Creation date: </b>" + metaImage.getDate());
                if(validated)
                    link.setHTML("<b>Link: </b>" + HandyTools.getAnchor("picture", Integer.toString(realId), "Right click to copy"));
                else
                    link.setHTML("<b>Link: </b><a href=./Mindlevel.html#picture="+realId+"&validated=false>Right click to copy</a>");
                fetchMission(metaImage.getMissionId());
                getScore(realId);
                commentPanel.add(new CommentSection(metaImage.getThreadId()));
                backPanel.setVisible(true);
                HandyTools.setLoading(false);
            }
        });
    }

    private String buildTagHTML(ArrayList<String> tags) {
        String tagHtml = "<b>Tags: </b>";
        if(tags!=null)
            for(String tag : tags) {
                tagHtml = tagHtml.concat(HandyTools.getAnchor("user", tag, tag));
                if(tags.get(tags.size()-1)!=tag)
                    tagHtml = tagHtml.concat(",&nbsp;");
            }
        return tagHtml;
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

    private void getVoteValue() {
        ratingService.getVoteValue(Mindlevel.user.getUsername(), realId, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading the votes."));
            }

            @Override
            public void onSuccess(Integer result) {
                if(result != 0) {
                    rating.setValue((int)result);
                    rating.setReadOnly(true);
                } else {
                    rating.setValue(0);
                    rating.setReadOnly(false);
                }
            }
        });
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
                    mission.setHTML("<b>Mission: </b>" + HandyTools.getAnchor("mission", Integer.toString(m.getId()), m.getName()));
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

    private void getScore(final int id) {
        ratingService.getScore(id, new AsyncCallback<Double>() {

            @Override
            public void onFailure(Throwable caught) {
                score.setHTML("<b>Score: </b> No votes yet.");
            }

            @Override
            public void onSuccess(final Double totalScore) {
                ratingService.getVoteNumber(id, new AsyncCallback<Integer>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        score.setHTML("<b>Score: </b> No votes yet.");
                    }

                    @Override
                    public void onSuccess(Integer votes) {
                        score.setHTML("<b>Score: </b>" + totalScore + "/5 of " + votes + " votes");
                    }
                });
            }
        });
    }
}