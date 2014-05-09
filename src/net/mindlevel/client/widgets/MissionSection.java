package net.mindlevel.client.widgets;

import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class MissionSection extends Composite {

    private final FlowPanel p;
    private final LoadingElement l = new LoadingElement();
    private final Constraint constraint;
    private final int start;
    private final int offset;

    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    /**
     * Constructs a MissionSection that controls a number of MissionElement
     *
     */
    public MissionSection(final Constraint constraint) {
        this(0, 100, constraint);
    }

    public MissionSection(int start, int offset, Constraint constraint) {
        this.constraint = constraint;
        this.start = start;
        this.offset = offset;
        p = new FlowPanel();
        p.add(l);
        init();

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("gallery-section");
    }

    public void init() {
        missionService.getMissions(start, offset, constraint,
                new AsyncCallback<List<Mission>>() {

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(List<Mission> missions) {
                if(missions.size() > 0) {
                    for(Mission m : missions) {
                        p.add(new MissionElement(m));
                    }
                } else {
                    p.add(new HTML("No missions found."));
                }
                l.removeFromParent();
            }
        });
    }
}