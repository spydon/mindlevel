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

    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    /**
     * Constructs a MissionSection that controls a number of ReadBox and WriteBox
     *
     */
    public MissionSection(final Constraint constraint) {
        p = new FlowPanel();
        final LoadingElement l = new LoadingElement();
        p.add(l);

        missionService.getMissions(0, 100, constraint,
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
                }
                l.removeFromParent();
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("gallery-section");
    }
}