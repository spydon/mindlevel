package net.mindlevel.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class VotingSection extends Composite {

    private final HorizontalPanel backPanel = new HorizontalPanel();

    /**
     * Constructs a CotingSection that contains an UpVoteButton and a DownVoteButton
     *
     */
    public VotingSection(final int pictureId) {
        backPanel.add(new VoteButton(pictureId, true));
        backPanel.add(new VoteButton(pictureId, false));

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);

        // Give the overall composite a style name.
        setStyleName("voting-section");
    }
}