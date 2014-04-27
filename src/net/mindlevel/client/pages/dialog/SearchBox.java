package net.mindlevel.client.pages.dialog;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class SearchBox {
    private final DialogBox popup;

    public SearchBox() {
        popup = new DialogBox(true);
        init();
    }

    private void init() {
        FlexTable t = new FlexTable();
        Label searchTypeL = new Label("Search for: ");
        ListBox searchTypeLB = new ListBox();
        searchTypeLB.addItem("Username");
        searchTypeLB.addItem("Picture");
        searchTypeLB.addItem("Mission");
        searchTypeLB.addStyleName("fullwidth");


        TextBox searchBox = new TextBox();
        searchBox.addStyleName("search-box");
        searchBox.getElement().setPropertyString("placeholder", "Search...");
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
        t.setWidget(0, 0, searchTypeL);
        t.setWidget(0, 1, searchTypeLB);
        t.setWidget(1, 0, searchBox);
        t.setWidget(2, 0, searchButton);
        t.setWidget(3, 0, cancelButton);
        t.getFlexCellFormatter().setColSpan(1, 0, 2);
        t.getFlexCellFormatter().setColSpan(2, 0, 2);
        t.getFlexCellFormatter().setColSpan(3, 0, 2);

        popup.setText("Search");
        popup.add(t);
        popup.center();
        popup.show();
    }
}
