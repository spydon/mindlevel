package net.mindlevel.mobile.client.view;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.widgets.MissionElement;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressBar;

public class MissionView extends MPage {
    protected VerticalPanel main;

    /**
     * Create a remote service proxy to talk to the server-side user
     * service.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public MissionView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    public void init() {
    }

    private void loadMission(int id) {
        main.clear();
        final ProgressBar progress = new ProgressBar();
        main.add(progress);
        missionService.getMission(id, true, new AsyncCallback<Mission>() {
            @Override
            public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(Mission mission) {
                main.remove(progress);
                MissionElement missionElement = new MissionElement(mission);
                main.add(missionElement);
            }
        });
    }

    @Override
    public Widget asWidget() {
        return main;
    }

    @Override
    public void setId(int id) {
        loadMission(id);
    }

    @Override
    public void setId(String id) {
        setId(Integer.parseInt(id));
    }
}