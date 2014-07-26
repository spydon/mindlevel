package net.mindlevel.mobile.client.view;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.services.RatingService;
import net.mindlevel.client.services.RatingServiceAsync;
import net.mindlevel.mobile.client.MetaImageElement;
import net.mindlevel.mobile.client.MindlevelMobile;
import net.mindlevel.mobile.client.widget.MindlevelImageButton;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
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
    protected AboutImageButton title;
    protected Carousel carousel;

    private PreviousitemImageButton previous;
    private NextitemImageButton next;
    private MindlevelImageButton home;
    private GoodImageButton voteUp;
    private BadImageButton voteDown;

    private int id = 0;
    private int realId = 0;
    private int loadedId = Integer.MAX_VALUE;
    private int imageCount = 0;
    private boolean initialized = false;

    private final ArrayList<MetaImageElement> loadedImages;
    private HandlerRegistration handlerRegistration;

    /**
     * Create a remote service proxy to talk to the server-side picture
     * service.
     */
    private final PictureServiceAsync pictureService = GWT
            .create(PictureService.class);

    private final RatingServiceAsync ratingService = GWT
            .create(RatingService.class);


    public PictureView() {
        main = new RootFlexPanel();
        loadedImages = new ArrayList<MetaImageElement>();
    }

    private void init() {
        bar = new ButtonBar();
        title = new AboutImageButton();
        carousel = new Carousel();

        previous = new PreviousitemImageButton();
        next = new NextitemImageButton();
        home = new MindlevelImageButton();
        voteUp = new GoodImageButton();
        voteDown = new BadImageButton();

        bar.addStyleName("m-bar");
        title.setStylePrimaryName("m-bar-title");

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

        home.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("");
            }
        });

        voteUp.addTapHandler(new VoteTapHandler(true));
        voteDown.addTapHandler(new VoteTapHandler(false));

        handlerRegistration = carousel.addSelectionHandler(new CarouselHandler());

        title.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("pictureinfo=" + realId);
            }
        });

        initialized = true;

        bar.setJustification(Justification.CENTER);
        bar.add(previous);
        bar.add(voteDown);
        bar.add(home);
        bar.add(title);
//        bar.add(about);
        bar.add(voteUp);
        bar.add(next);
//        main.add(imagePanel);
        main.add(carousel);
        main.add(bar);
    }

    private void reInit() {
        id = 0;
        imageCount = 0;
        loadedId = Integer.MAX_VALUE;
        loadedImages.clear();
        carousel.clear();
        handlerRegistration.removeHandler();
        handlerRegistration = carousel.addSelectionHandler(new CarouselHandler());
        loadImage(realId, false);
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
                        String text = title.getText();
                        if(text.contains("(")) {
                            int total = Integer.parseInt(text.substring(text.indexOf("(")+1, text.length()-1));
                            total += isUpVote ? 1 : -1;
                            text = text.substring(0, text.indexOf("(")-1);
                            title.setText(text + "(" + total + ")");
                            loadedImages.get(carousel.getSelectedPage()).getMetaImage().setScore(total);
                        }
                    }
                });
            } else {
                HandyTools.notLoggedInBox();
            }
        }
    }

    private class CarouselHandler implements SelectionHandler<Integer> {
        int lastImageNum;
        int imageNum = -1;
        boolean ignoreThisTime = true;

        @Override
        public void onSelection(SelectionEvent<Integer> arg0) {
            lastImageNum = imageNum;
            imageNum = arg0.getSelectedItem();
            if(imageNum == 0 &&
                    lastImageNum == imageNum &&
                    loadedImages.get(0).getMetaImage().getRelativeId() != loadedImages.get(0).getMetaImage().getImageCount()) {
                if(ignoreThisTime) {
                    ignoreThisTime = false;
                } else {
                    setId(0);
                    reInit();
                }
            } else if(lastImageNum != imageNum) {
                MetaImageElement imageElement = loadedImages.get(imageNum);
                MetaImage metaImage = imageElement.getMetaImage();

                if(imageElement.isLoaded()) {
                    loadedImages.get(imageNum).adjustSize();
                }

                id = metaImage.getRelativeId();
                realId = metaImage.getId();
                HomeView.pictureId = metaImage.getId();

                //Remember that the carousel is reversed compared to the website
                //Check if the right arrow is needed
                if (id == 1) {
                    next.setVisible(false);
                } else if(!next.isVisible()) {
                    next.setVisible(true);
                }

                //Check if the left arrow is needed
                if (id == imageCount) {
                    previous.setVisible(false);
                } else if(!previous.isVisible()) {
                    previous.setVisible(true);
                }

                //If it is a 'notfound' picture
                if (imageCount == 0) {
                    previous.setVisible(false);
                    next.setVisible(false);
                }

                History.newItem("picture=" + realId, false);

                String text = metaImage.getTitle().length() > 7 ? metaImage.getTitle().substring(0,7)+".." : metaImage.getTitle();
                title.setText(text + "(" + metaImage.getScore() + ")");

                //Load next image
                if(loadedId > 1 && loadedImages.size() < imageNum+2) {
                    loadImage(loadedId-1, true);
                }
            }
        }
    }

    private int isLoaded(int id) {
        int place = -1;
        for(int x = 0; x < loadedImages.size(); x++) {
            if(loadedImages.get(x).getMetaImage().getId() == id) {
                place = x;
                break;
            }
        }
        return place;
    }

    private void show() {
        if(!initialized) {
            init();
            loadImage(realId, false);
        } else if(isLoaded(realId) == -1) {
            reInit();
        } else {
            carousel.setSelectedPage(isLoaded(realId));
        }
    }

    private void nextImage() {
        if (id > 1) {
            carousel.setSelectedPage((carousel.getSelectedPage()+1));
        }
    }

    private void prevImage() {
        if (id < imageCount) {
            carousel.setSelectedPage(carousel.getSelectedPage()-1);
        } else {
            carousel.refresh();
        }
    }

    private void loadImage(final int id, final boolean relative) {
        pictureService.get(id, relative, UserTools.isAdult(), true, new AsyncCallback<MetaImage>() {

            @Override
            public void onFailure(Throwable caught) {
                History.newItem("");
                HandyTools.showDialogBox("Error", "No picture with id " + realId + " was found.");
            }

            @Override
            public void onSuccess(final MetaImage metaImage) {
                SimplePanel imageContainer = new SimplePanel();
                imageContainer.addStyleName("m-image-container");

                MetaImageElement imageElement = new MetaImageElement(metaImage);
                loadedId = (metaImage.getRelativeId() < loadedId) ? metaImage.getRelativeId() : loadedId;
                imageCount = metaImage.getImageCount();
                loadedImages.add(imageElement);

                imageContainer.add(imageElement);
                carousel.add(imageContainer);
                carousel.refresh();
            }
        });
    }

    public void setId(int id) {
        this.realId = id;
    }

    @Override
    protected void onLoad() {
        MindlevelMobile.hideBar();
    }

    @Override
    public Widget asWidget() {
        setId(Integer.parseInt(parameter));
        show();
        onLoad();
        return main;
    }
}
