package net.mindlevel.client.pages;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.client.widgets.MissionSection;
import net.mindlevel.shared.Constraint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Missions {
    private final Panel appArea;
    private int current = 0;
    private int missionCount = 0;
    private int pageCount = 0;
    private final int stepSize = 20;
    private Label pageL, totalL;
    private Button leftButton, rightButton, suggestButton;
    private final SimplePanel missionsContainer = new SimplePanel();
    private final Constraint constraint;
    private boolean hasSuggestionButton = false;
    /**
     * The list of data to display.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public Missions(Panel appArea) {
        this(appArea, new Constraint());
    }

    public Missions(Panel appArea, Constraint constraint) {
        this.appArea = appArea;
        this.constraint = constraint;
        init();
    }

    public void init() {
        constraint.setSortingColumn("timestamp desc");
        MissionSection missionSection = new MissionSection(current, stepSize, constraint);
        missionsContainer.add(missionSection);

        HTML explanation = new HTML("<h1>Missions</h1><p>Click on a mission to see more information or to upload a picture.</p>");
        explanation.addStyleName("max-95");
        appArea.add(explanation);
        appArea.add(missionsContainer);

        leftButton = new Button("<");
        rightButton = new Button(">");
        pageL = new Label("1");
        Label separatorL = new Label("/");
        totalL = new Label("");
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        leftButton.setEnabled(false);

        HorizontalPanel pageController = new HorizontalPanel();
        pageController.add(pageL);
        pageController.add(separatorL);
        pageController.add(totalL);
        countMissions();

        appArea.add(buttonPanel);
        appArea.add(pageController);

        rightButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                missionsContainer.clear();
                current = current+stepSize+1;
                missionsContainer.add(new MissionSection(current, stepSize, constraint));
                if(current >= missionCount-stepSize) {
                    rightButton.setEnabled(false);
                }
                leftButton.setEnabled(true);
            }
        });

        leftButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                missionsContainer.clear();
                current = current-stepSize-1;
                missionsContainer.add(new MissionSection(current, stepSize, constraint));
                if(current <= 0) {
                    current = 0;
                    leftButton.setEnabled(false);
                }
                if(current < missionCount) {
                    rightButton.setEnabled(true);
                }
            }
        });

        addSuggestionButton();
    }

    public void addSuggestionButton() {
        if(UserTools.isLoggedIn() && !hasSuggestionButton) {
            hasSuggestionButton = true;
            suggestButton = new Button("Suggest Mission");
            suggestButton.addStyleName("suggest-mission-button");
            suggestButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    History.newItem("missionsuggestion");
                }
            });
            appArea.add(suggestButton);
        }
    }

    public void removeSuggestionButton() {
        if(suggestButton != null) {
            suggestButton.removeFromParent();
        }
    }

    private void countMissions() {
        missionService.getMissionCount(UserTools.isAdult(), constraint.isValidated(), new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(Integer result) {
                missionCount = result;
                pageCount = (int) Math.ceil((double)missionCount/stepSize);
                if(current >= missionCount-stepSize) {
                    rightButton.setEnabled(false);
                }
                totalL.setText("" + pageCount);
            }
        });
    }
}