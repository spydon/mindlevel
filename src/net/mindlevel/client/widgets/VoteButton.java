package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.UserTools;
import net.mindlevel.client.services.RatingService;
import net.mindlevel.client.services.RatingServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VoteButton extends Composite {

    private final VerticalPanel p;
    private final String upVotePath = "../images/icons/vote-up.svg";
    private final String downVotePath = "../images/icons/vote-down.svg";

    private final RatingServiceAsync ratingService = GWT
            .create(RatingService.class);

    /**
     * Constructs an UpVoteButton
     *
     */
    public VoteButton(final int pictureId, final boolean isUpVote) {
        p = new VerticalPanel();

        Image image = new Image(isUpVote ? upVotePath : downVotePath);
        final HTML total = new HTML();

        image.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                ratingService.setVoteValue(UserTools.getToken(), pictureId, isUpVote, new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable fail) {
                        HandyTools.showDialogBox("Error", new HTML(fail.getMessage()));
                    }

                    @Override
                    public void onSuccess(Void arg0) {
                        total.setHTML("" + (Integer.parseInt(total.getHTML())+(isUpVote ? 1 : -1)));
                    }
                });
            }
        });

        p.add(image);
        p.add(total);

        ratingService.getVoteNumber(pictureId, isUpVote, !isUpVote, new AsyncCallback<Integer>() {

            @Override
            public void onSuccess(Integer result) {
                total.setHTML("" + result);
            }

            @Override
            public void onFailure(Throwable failure) {
                //Could not fetch vote number
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        image.setStyleName("vote-button");
    }
}