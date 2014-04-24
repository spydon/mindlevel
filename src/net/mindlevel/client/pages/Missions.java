package net.mindlevel.client.pages;

import java.util.List;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.pages.dialog.MissionSuggestion;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.Normalizer;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;

public class Missions {
    private final RootPanel appArea;
    private final CellTable<Mission> table = new CellTable<Mission>();
    private AsyncDataProvider<Mission> provider;
    private boolean validated = true;
    /**
     * The list of data to display.
     */
    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public Missions(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    public Missions(RootPanel appArea, boolean validated) {
        this.appArea = appArea;
        this.validated = validated;
        init();
    }

    public void init() {
        //Sets how many elements to show on each page
        table.setPageSize(10);

        //Set style name of the table
        table.setStyleName("pointer-table");

        //Makes it possible to get to the mission profiles by clicking the row of the mission
        table.addCellPreviewHandler(new Handler<Mission>() {

            @Override
            public void onCellPreview(CellPreviewEvent<Mission> event) {
                boolean isClick = "click".equals(event.getNativeEvent().getType());
                if(isClick) {
                    new MissionProfile(appArea, event.getValue().getId(), validated);
                }
            }
        });
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

        // Add a text column to show the name.
        TextColumn<Mission> nameColumn = new TextColumn<Mission>() {
            @Override
            public String getValue(Mission mission) {
                return mission.getName();
            }
        };
        nameColumn.setSortable(true);
        table.addColumn(nameColumn, "Name");

        // Add a category column to show the category.
        TextColumn<Mission> categoryColumn = new TextColumn<Mission>() {
            @Override
            public String getValue(Mission mission) {
                return Normalizer.listToString(mission.getCategories());
            }
        };
        table.addColumn(categoryColumn, "Categories");

        // Add a category column to show if mission is only suited for adults.
        TextColumn<Mission> adultColumn = new TextColumn<Mission>() {
            @Override
            public String getValue(Mission mission) {
                if(mission.getAdult()) {
                    return "Yes";
                } else {
                    return "No";
                }
            }
        };
        table.addColumn(adultColumn, "Adult Only?");


        // Add a category column to show if mission is only suited for adults.
        TextColumn<Mission> creatorColumn = new TextColumn<Mission>() {
            @Override
            public String getValue(Mission mission) {
                return mission.getCreator();
            }
        };
        table.addColumn(creatorColumn, "Creator");

        // Add a category column to show if mission is only suited for adults.
        TextColumn<Mission> createdColumn = new TextColumn<Mission>() {
            @Override
            public String getValue(Mission mission) {
                return Normalizer.normalizeDate(mission.getTimestamp());
            }
        };
        table.addColumn(createdColumn, "Created");
        // Add a date column to show the last time the mission was accomplished.
//        DateCell dateCell = new DateCell();
//        Column<User, Date> dateColumn = new Column<User, Date>(dateCell) {
//            @Override
//            public Date getValue(User user) {
//                return new Date(Long.parseLong(user.getLastLogin()));
//            }
//        };
//        dateColumn.setSortable(true);
//        table.addColumn(dateColumn, "Last login");

        // Manages the pages
        final SimplePager pager = new SimplePager();
        pager.setDisplay(table);

        // Associate an async data provider to the table
        provider = new AsyncDataProvider<Mission>() {
            @Override
            protected void onRangeChanged(HasData<Mission> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<List<Mission>> callback = new AsyncCallback<List<Mission>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                    }

                    @Override
                    public void onSuccess(List<Mission> result) {
                        if(result != null)
                            updateRowData(start, result);
                    }
                };
                missionService.getMissions(start, length, UserTools.isAdult(), validated, callback);
            }
        };

        countMissions();
        provider.addDataDisplay(table);
        appArea.add(table);
        appArea.add(new HTML("(Click on the table to get more information about a mission)"));
        appArea.add(pager);

        if(UserTools.isLoggedIn()) {
            Button suggestButton = new Button("Suggest Mission");
            suggestButton.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    new MissionSuggestion();
                }
            });
            appArea.add(suggestButton);
        }
    }

    private void countMissions() {
        missionService.getMissionCount(UserTools.isAdult(), validated, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(Integer result) {
                provider.updateRowCount(result, true);
            }
        });
    }
}