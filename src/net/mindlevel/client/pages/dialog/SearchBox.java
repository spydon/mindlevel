package net.mindlevel.client.pages.dialog;


import net.mindlevel.shared.Category;
import net.mindlevel.shared.Normalizer;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class SearchBox {
    private final DialogBox popup;
    private final ListBox searchTypeLB = new ListBox();
    private final ListBox categoryLB = new ListBox();
    private final TextBox searchBox = new TextBox();

    public SearchBox() {
        popup = new DialogBox(true);
        init();
    }

    private void init() {
        FlexTable t = new FlexTable();
        Label searchTypeL = new Label("Search for: ");
        searchTypeLB.addItem("User");
        searchTypeLB.addItem("Picture");
        searchTypeLB.addItem("Mission");
        searchTypeLB.addStyleName("fullwidth");

        final Label categoryL = new Label("Category: ");
        for(Category category : Category.values() ) {
            categoryLB.addItem(Normalizer.capitalizeName(category.toString()));
        }
        categoryL.setVisible(false);
        categoryLB.setVisible(false);

        searchTypeLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                String type = searchTypeLB.getValue(searchTypeLB.getSelectedIndex()).toLowerCase();
                if(type.equals("user")) {
                    categoryL.setVisible(false);
                    categoryLB.setVisible(false);
                } else {
                    categoryL.setVisible(true);
                    categoryLB.setVisible(true);
                }
            }
        });

        searchBox.addStyleName("search-box");
        searchBox.getElement().setPropertyString("placeholder", "Search...");
        searchBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    search();
                }
            }
        });
        Button searchButton = new Button("Search");
        Button cancelButton = new Button("Cancel");
        searchButton.addStyleName("fullwidth");
        cancelButton.addStyleName("fullwidth");
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                popup.hide();
            }
        });

        searchButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                search();
            }
        });

        t.setWidget(0, 0, searchTypeL);
        t.setWidget(0, 1, searchTypeLB);
        t.setWidget(1, 0, categoryL);
        t.setWidget(1, 1, categoryLB);
        t.setWidget(2, 0, searchBox);
        t.setWidget(3, 0, searchButton);
        t.setWidget(4, 0, cancelButton);
        t.getFlexCellFormatter().setColSpan(2, 0, 2);
        t.getFlexCellFormatter().setColSpan(3, 0, 2);
        t.getFlexCellFormatter().setColSpan(4, 0, 2);

        popup.setText("Search");
        popup.add(t);
        popup.center();
        popup.show();
    }

    private void search() {
        String type = searchTypeLB.getValue(searchTypeLB.getSelectedIndex()).toLowerCase();
        String category = categoryLB.getValue(categoryLB.getSelectedIndex()).toLowerCase();
        String query = searchBox.getText();
        switch(type) {
        case "user":
            History.newItem("search=" + type + "&u=" + query);
            break;
        case "picture":
            History.newItem("search=" + type + "&p=" + query + "&c=" + category);
            break;
        case "mission":
            History.newItem("search=" + type + "&m=" + query + "&c=" + category);
            break;
        }
    }
}
