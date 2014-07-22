package net.mindlevel.mobile.client.view;

import java.util.ArrayList;
import java.util.HashSet;

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
import net.mindlevel.mobile.client.MetaImageElement;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.image.AboutImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.BadImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.GoodImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.NextitemImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.PreviousitemImageButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.carousel.Carousel;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPropertyHelper.Justification;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;


public class PictureView extends MPage {
    protected RootFlexPanel main;
    protected ButtonBar bar;
    protected HTML title;
    protected Carousel carousel;

    private PreviousitemImageButton previous;
    private NextitemImageButton next;
    private AboutImageButton about;
    private GoodImageButton voteUp;
    private BadImageButton voteDown;

    private int id = 0;
    private int realId = 0;
    private int startId = 0;
    private int loadedId = Integer.MAX_VALUE;
    private int imageCount = 0;
    private final String notFoundPath = Mindlevel.PATH + "images/notfound.jpg";
    private boolean notFound = false;
    private boolean initialized = false;

    private final ArrayList<MetaImageElement> loadedImages;

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
        loadedImages = new ArrayList<MetaImageElement>();
    }

    private void init() {
        System.out.println("Init");
        bar = new ButtonBar();
        title = new HTML();
        carousel = new Carousel();

//        carousel.addStyleName("m-image-panel");
        bar.addStyleName("m-bar");
        voteTotal.addStyleName("m-vote-total");

        previous = new PreviousitemImageButton();
        next = new NextitemImageButton();
        about = new AboutImageButton();
        voteUp = new GoodImageButton();
        voteDown = new BadImageButton();

        carousel.setShowCarouselIndicator(false);

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

        voteUp.addTapHandler(new VoteTapHandler(true));
        voteDown.addTapHandler(new VoteTapHandler(false));

        carousel.addSelectionHandler(new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent<Integer> arg0) {
                int imageNum = arg0.getSelectedItem();
                System.out.println("Beginning of selection Real id: " + realId + " imageNum: " + imageNum);


                MetaImageElement imageElement = loadedImages.get(imageNum);
                MetaImage metaImage = imageElement.getMetaImage();

                if(imageElement.isLoaded()) {
                    loadedImages.get(imageNum).adjustSize();
                }

                id = metaImage.getRelativeId();
                realId = metaImage.getId();

                //Remember that the carousel is reversed compared to the website
                //Check if the right arrow is needed
                if (id == 1) {
                    next.setVisible(false);
                } else if(!next.isVisible()) {
                    next.setVisible(true);
                }

                //Check if the left arrow is needed
                if (id == imageCount || metaImage.getId() == startId) {
                    previous.setVisible(false);
                } else if(!previous.isVisible()) {
                    previous.setVisible(true);
                }

                //If it is a 'notfound' picture
                if (imageCount == 0) {
                    previous.setVisible(false);
                    next.setVisible(false);
                }

                System.out.println("Real id in selection: " + realId);
                History.newItem("picture=" + realId, false);

                title.setHTML(metaImage.getTitle());

                ratingService.getVoteNumber(realId, true, true, new AsyncCallback<Integer>() {

                    @Override
                    public void onSuccess(Integer result) {
                        voteTotal.setText("" + result);
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        //Could not fetch vote number
                    }
                });

                //Load next image
                if(loadedId > 1 && loadedImages.size() < imageNum+2) {
                    System.out.println("Tries to load image");
                    loadImage(loadedId-1, true);
                }
            }
        });

        initialized = true;

        bar.setJustification(Justification.CENTER);
        bar.add(previous);
        bar.add(voteDown);
        bar.add(title);
        bar.add(voteTotal);
//        bar.add(about);
        bar.add(voteUp);
        bar.add(next);
//        main.add(imagePanel);
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
        if(!initialized) {
            startId = realId;
            System.out.println("Going for init");
            init();
            loadImage(realId, false);
        } else if(loadedImages.get(carousel.getSelectedPage()).getMetaImage().getId() != realId) {
            startId = realId;
            System.out.println("Going for clear");
            loadedId = Integer.MAX_VALUE;
            loadedImages.clear();
            carousel.clear();
            loadImage(realId, false);
        } else {
            System.out.println("Going outside");
        }
    }

    private void randomImage() {
        loadImage(-1, true);
    }

    private void nextImage() {
        if (id > 1) {
            carousel.setSelectedPage((carousel.getSelectedPage()+1));
        }
    }

    private void prevImage() {
        if (id < imageCount) {
            carousel.setSelectedPage(carousel.getSelectedPage()-1);
        }
    }

    private void loadImage(final int id, final boolean relative) {
        pictureService.get(id, relative, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {

            @Override
            public void onFailure(Throwable caught) {
//                setImageUrl(notFoundPath);
                notFound = true;
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                System.out.println("Beginning of load Real id: " + realId);
                SimplePanel imageContainer = new SimplePanel();
                imageContainer.addStyleName("m-image-container");

                MetaImageElement imageElement = new MetaImageElement(metaImage);
                loadedId = (metaImage.getRelativeId() < loadedId) ? metaImage.getRelativeId() : loadedId;
                imageCount = metaImage.getImageCount();
                loadedImages.add(imageElement);

                imageContainer.add(imageElement);
                carousel.add(imageContainer);
//                System.out.println("ImageCount: " + metaImage.getImageCount() + " ID: " + metaImage.getId() + " LoadedId: " + loadedId + " InCarousel: " + carousel.getScrollPanel().getPagesX().length());
                carousel.refresh();
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
        this.realId = id;
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
