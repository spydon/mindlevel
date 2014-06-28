package net.mindlevel.mobile.client.view;

import java.util.HashSet;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.services.RatingService;
import net.mindlevel.client.services.RatingServiceAsync;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.util.ImageLoader;
import com.googlecode.mgwt.ui.client.util.IsImage;
import com.googlecode.mgwt.ui.client.widget.button.image.AboutImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.BadImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.GoodImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.NextitemImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.PreviousitemImageButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.carousel.Carousel;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPropertyHelper.Justification;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;

public class PictureView implements MPage {
    protected RootFlexPanel main;
    protected ButtonBar bar;
    protected HTML title;
    protected RootFlexPanel imagePanel;
    protected Carousel carousel;
    protected Image image;
    protected ImageLoader imageLoader;
    protected ImageLoaderCallback imageLoaderCallback;

    private PreviousitemImageButton next;
    private NextitemImageButton previous;
    private AboutImageButton about;
    private GoodImageButton voteUp;
    private BadImageButton voteDown;

    private int id = 0;
    private int realId = 1;
    private int imageCount = Integer.MAX_VALUE;
    private final String notFoundPath = Mindlevel.PATH + "images/notfound.jpg";
    private boolean notFound = false;

    private final Label voteTotal = new Label("0");

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

    private final RatingServiceAsync ratingService = GWT
            .create(RatingService.class);


    public PictureView() {
        main = new RootFlexPanel();
        bar = new ButtonBar();
        title = new HTML();
        carousel = new Carousel();
        imagePanel = new RootFlexPanel();
        image = new Image();
        imageLoader = new ImageLoader();
        imageLoaderCallback = new ImageLoaderCallback();

        carousel.addStyleName("m-carousel");
        imagePanel.addStyleName("m-image-panel");
        image.addStyleName("m-image");
        bar.addStyleName("m-bar");
        voteTotal.addStyleName("m-vote-total");

        init();
    }

    private void init() {
        next = new PreviousitemImageButton();
        previous = new NextitemImageButton();
        about = new AboutImageButton();
        voteUp = new GoodImageButton();
        voteDown = new BadImageButton();

        previous.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                prevImage();
            }
        });

        next.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                nextImage();
            }
        });

        image.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                randomImage();
            }
        });

        voteUp.addTapHandler(new VoteTapHandler(true));
        voteDown.addTapHandler(new VoteTapHandler(false));

        //Click image will be random

        bar.setJustification(Justification.CENTER);
        bar.add(next);
        bar.add(voteDown);
        bar.add(voteTotal);
//        bar.add(about);
        bar.add(voteUp);
        bar.add(previous);
        carousel.add(imagePanel);
        carousel.add(new Image("http://127.0.0.1:8888/pictures/04e798d5-0f9f-443a-ba48-02b883f062b7_scaled.JPG"));
        main.add(carousel);
        main.add(bar);
    }

    private class VoteTapHandler implements TapHandler {
        private final boolean isUpVote;

        public VoteTapHandler(boolean isUpVote) {
            this.isUpVote = isUpVote;
        }

        @Override
        public void onTap(TapEvent event) {
            if(UserTools.isLoggedIn()) {
                ratingService.setVoteValue(UserTools.getToken(), realId, isUpVote, new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable fail) {
                        HandyTools.showDialogBox("Error", new HTML(fail.getMessage()));
                    }

                    @Override
                    public void onSuccess(Void arg0) {
                        voteTotal.setText("" + (Integer.parseInt(voteTotal.getText())+(isUpVote ? 1 : -1)));
                    }
                });
            } else {
                HandyTools.notLoggedInBox();
            }
        }
    }

    private void show() {
        loadImage(id, false);
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
        pictureService.get(id, relative, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {
            @Override
            public void onFailure(Throwable caught) {
                setImageUrl(notFoundPath);
                notFound = true;
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                setImageUrl(Mindlevel.PATH + "pictures/" + metaImage.getFilename());
                imageCount = metaImage.getImageCount();
                if(id == 0) {
                    setId(imageCount);
                } else if(!relative || id == -1) {
                    setId(metaImage.getRelativeId());
                }

                //Check if the left arrow is needed
                if (getId() == 1) {
                    previous.setVisible(false);
                } else if(!previous.isVisible()) {
                    previous.setVisible(true);
                }

                //Check if the right arrow is needed
                if (getId() == imageCount) {
                    next.setVisible(false);
                } else if(!next.isVisible()) {
                    next.setVisible(true);
                }

                //If it is a 'notfound' picture
                if (imageCount == 0) {
                    next.setVisible(false);
                    previous.setVisible(false);
                }

                //If the image is validated, fetch the votevalue, else add validation button
                realId = metaImage.getId();
                History.newItem("picture=" + realId, false);

                title.setHTML(metaImage.getTitle());

                ratingService.getVoteNumber(realId, true, true, new AsyncCallback<Integer>() {

                    @Override
                    public void onSuccess(Integer result) {
                        voteTotal.setText("" + result);
                        System.out.println(result);
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        //Could not fetch vote number
                    }
                });
            }
        });
    }

    private void initInfo(MetaImage metaImage) {
        //location.setHTML("<b>Location: </b>" + metaImage.getLocation());
        //owner.setHTML("<b>Owner: </b>" + HandyTools.getAnchor("user", metaImage.getOwner(), metaImage.getOwner()));
        //description.setHTML("<h1>Description</h1><br>"
        //        + HandyTools.formatHtml(metaImage.getDescription()));
        //tags.setHTML(buildTagHTML(metaImage.getTags()));
        //date.setHTML("<b>Creation date: </b>" + metaImage.getDate());
        //link.setHTML("<b>Link: </b>" + HandyTools.getAnchor("picture", Integer.toString(realId), "Right click to copy"));
        //fetchMission(metaImage.getMission().getId());
    }

    private String buildTagHTML(HashSet<String> tags) {
        String separator = ",&nbsp;";
        String tagHtml = "<b>Tags: </b>";
        if(tags!=null && !tags.isEmpty()) {
            for(String tag : tags) {
                tagHtml = tagHtml.concat(HandyTools.getAnchor("user", tag, tag));
                tagHtml = tagHtml.concat(separator);
            }
            tagHtml = tagHtml.substring(0, tagHtml.length()-separator.length());
        }
        return tagHtml;
    }

    private void setImageUrl(final String url) {
        imagePanel.clear();
        imagePanel.add(new LoadingElement());
        imageLoader.loadImage(url, imageLoaderCallback);
    }

    private class ImageLoaderCallback implements AsyncCallback<IsImage> {
        @Override
        public void onFailure(Throwable arg0) {
            //Report that something went wrong
        }

        @Override
        public void onSuccess(IsImage loaded) {
            imagePanel.clear();
            image.setUrl(loaded.getElement().getSrc());
            imagePanel.add(image);
        }
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
//                    mission.setHTML("<b>Mission: </b>" + HandyTools.getAnchor("mission", Integer.toString(m.getId()), m.getName()));
//                    category.setHTML("<b>Categories: </b>" + HandyTools.getCategoryAnchors(m.getCategories()));
                }
            }
        });
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Widget asWidget() {
        show();
        return main;
    }

    @Override
    public void setId(String id) {
        setId(Integer.parseInt(id));
    }
}
