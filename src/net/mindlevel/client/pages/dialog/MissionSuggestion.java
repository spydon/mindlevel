package net.mindlevel.client.pages.dialog;


import java.util.ArrayList;
import java.util.HashSet;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.FieldVerifier;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MissionSuggestion {
    private FlexTable t;
    private final HTML header;
    private final DecoratedPopupPanel popup;
    private final VerticalPanel panel;
    private final ArrayList<ListBox> categoryList = new ArrayList<ListBox>();

    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public MissionSuggestion() {
        popup = new DecoratedPopupPanel(false);
        panel = new VerticalPanel();
        header = new HTML("<h1>Suggest Mission</h1>");
        init();
    }

    private void init() {
        // Initiate the FlexTable
        t = new FlexTable();

        panel.add(header);

        // Create a new uploader panel and attach it to the document
        FlexTable metaData = getMetaDataPanel();
        panel.add(metaData);

        popup.add(panel);
        popup.center();
        popup.show();
    }

    private FlexTable getMetaDataPanel() {
        Label titleL = new Label("Title");
        final TextBox titleTB = new TextBox();
//        Label categoryL = new Label("Categories (as few as possible)");
//        final ListBox categoryLB = new ListBox();
//        getCategories(categoryLB);
        Label descriptionL = new Label("Description");
        final TextArea descriptionTA = new TextArea();
        Label adultL = new Label("18+ only?");
        final CheckBox adultCB = new CheckBox();
        Button uploadB = new Button("Upload for validation");
        uploadB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if(UserTools.isLoggedIn()) {
                    HashSet<Category> categories = new HashSet<Category>();

                    for(ListBox categoryLB : categoryList) {
                        Category category = Category.valueOf(categoryLB.getItemText(categoryLB.getSelectedIndex()).toUpperCase());
                        if(!categories.contains(category)) { //&& (!categoryLB.isEnabled() || categoryList.size() == 1)
                            categories.add(category);
                        }
                    }

                    Mission mission = new Mission(titleTB.getText(), categories, descriptionTA.getText(), UserTools.getUsername(), adultCB.getValue(), false);
                    String reason = FieldVerifier.isValidMission(mission);
                    if(reason.equals("")) {
                        missionUpload(mission);
                    } else {
                        HandyTools.showDialogBox("Error", new HTML(reason));
                    }
                } else {
                    HandyTools.notLoggedInBox();
                }
            }
        });
        Button closeB = new Button("Close");
        closeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.hide();
            }
        });
        t.setWidget(0, 0, titleL);
        t.setWidget(0, 1, titleTB);
        t.setWidget(1, 0, descriptionL);
        t.setWidget(1, 1, descriptionTA);
//        t.setWidget(2, 0, categoryL);
        createTagRow(2);
//        t.setWidget(2, 1, categoryLB);
        t.setWidget(3, 0, adultL);
        t.setWidget(3, 1, adultCB);
        t.setWidget(4, 1, uploadB);
        t.setWidget(5, 1, closeB);
        return t;
    }

    private void missionUpload(final Mission mission) {
        missionService.uploadMission(mission, UserTools.getToken(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(Void noreturn) {
                popup.hide();
                HandyTools.showDialogBox("Success", new HTML("<h1>Successfully suggested a mission.</h1>"));
            }
        });
    }

    private void createTagRow(final int row) {
        Label tagL = new Label("Add category");
        final ListBox categoryLB = new ListBox();
        final Button newTagB = new Button("+");
        final Button delTagB = new Button("-");

        for(Category c : Category.values()) {
            if(c != Category.ALL) {
                categoryLB.addItem(c.toString());
            }
        }
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(newTagB);
        buttonPanel.add(delTagB);
        t.setWidget(row, 0, tagL);
        t.setWidget(row, 1, categoryLB);
        t.setWidget(row, 2, buttonPanel);
        categoryLB.setFocus(true);
        categoryList.add(categoryLB);
        newTagB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                categoryLB.setEnabled(false);
                if(t.getRowCount() < 9) {
                    Cell cell = t.getCellForEvent(event);
                    t.insertRow(cell.getRowIndex()+1);
                    createTagRow(cell.getRowIndex()+1);
                } else {
                    HandyTools.showDialogBox("Enough", new HTML("You can only choose 4 categories, usually you only need 1 or 2."));
                }
            }
        });
        delTagB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Cell cell = t.getCellForEvent(event);
                categoryList.remove(categoryLB);
                if(t.getRowCount() > 6) {
                    t.removeRow(cell.getRowIndex());
                } else {
                    categoryLB.setItemSelected(0, false);
                }
            }
        });
        categoryLB.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    newTagB.click();
                }
            }
        });
    }
}
