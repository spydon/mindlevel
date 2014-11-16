package net.mindlevel.client.pages;

import java.util.List;

import net.mindlevel.client.services.UserService;
import net.mindlevel.client.services.UserServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.shared.User;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;

public class Highscore {
    private final RootPanel appArea;
    private final CellTable<User> table = new CellTable<User>();
    private AsyncDataProvider<User> provider;
    /**
     * The list of data to display.
     */
    private final UserServiceAsync highscoreService = GWT
            .create(UserService.class);

    public Highscore(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    public void init() {
        table.setPageSize(10);
        //Set style name of the table
        table.setStyleName("pointer-table");
        table.addCellPreviewHandler(new Handler<User>() {

            @Override
            public void onCellPreview(CellPreviewEvent<User> event) {
                boolean isClick = "click".equals(event.getNativeEvent().getType());
                if(isClick) {
                    new Profile(appArea, event.getValue().getUsername());
                }
            }
        });
        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

//        // Add a number column to show the name.
//        TextColumn<User> rankColumn = new TextColumn<User>() {
//            @Override
//            public String getValue(User user) {
//                return
//            }
//        };

        // Add a text column to show the name.
        TextColumn<User> nameColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return user.getUsername();
            }
        };
        nameColumn.setSortable(true);
        table.addColumn(nameColumn, "Name");

        // Add a adult column to show the name.
        TextColumn<User> adultColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return Boolean.toString(user.isAdult());
            }
        };
        table.addColumn(adultColumn, "Adult");

        // Add a date column to show the last logged in date.
        TextColumn<User> dateColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return HandyTools.formatDate(user.getLastLogin());
            }
        };
        table.addColumn(dateColumn, "Last login");

        // Add a date column to show the last logged in date.
        TextColumn<User> scoreColumn = new TextColumn<User>() {
            @Override
            public String getValue(User user) {
                return "" + user.getScore();
            }
        };
        table.addColumn(scoreColumn, "Score");

        // Manages the pages
        final SimplePager pager = new SimplePager();
        pager.setDisplay(table);

        // Associate an async data provider to the table
        // XXX: Use AsyncCallback in the method onRangeChanged
        // to actually get the data from the server side
        provider = new AsyncDataProvider<User>() {
            @Override
            protected void onRangeChanged(HasData<User> display) {
                final int start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
                    }

                    @Override
                    public void onSuccess(List<User> result) {
                        updateRowData(start, result);
                    }
                };
                highscoreService.getHighscore(start, length, callback);
            }
        };
        countUsers();
        provider.addDataDisplay(table);
        appArea.add(table);
        appArea.add(pager);
        appArea.add(new HTML("(Click on the table to get more information about a user)"));
    }

    private void countUsers() {
        highscoreService.getUserCount(new AsyncCallback<Integer>() {
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