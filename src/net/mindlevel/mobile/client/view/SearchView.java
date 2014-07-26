package net.mindlevel.mobile.client.view;

import net.mindlevel.client.widgets.GallerySection;
import net.mindlevel.client.widgets.MissionSection;
import net.mindlevel.client.widgets.UserSection;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Constraint;
import net.mindlevel.shared.Normalizer;
import net.mindlevel.shared.SearchType;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.image.SearchImageButton;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.input.listbox.MListBox;

public class SearchView extends MPage {
    protected VerticalPanel main;
    private final SimplePanel resultPanel;
    private final MListBox typeLB;
    private final MListBox categoryLB;
    private final MTextBox searchBox;
    private final SearchImageButton searchButton;

    public SearchView() {
        main = new VerticalPanel();
        resultPanel = new SimplePanel();

        main.addStyleName("m-center");
        main.addStyleName("m-margin");
        resultPanel.addStyleName("m-result");

        typeLB = new MListBox();
        categoryLB = new MListBox();
        searchBox = new MTextBox();
        searchButton = new SearchImageButton();
        searchButton.setText("Search");
        searchButton.addStyleName("m-long-button");
        searchButton.setWidth("300px");

        typeLB.addItem("User");
        typeLB.addItem("Picture");
        typeLB.addItem("Mission");

        VerticalPanel formPanel = new VerticalPanel();
        formPanel.addStyleName("m-button-panel");

        final FormEntry typeEntry = new FormEntry("Type: ", typeLB);
        final FormEntry categoryEntry = new FormEntry("Category: ", categoryLB);
        final FormEntry searchEntry = new FormEntry("Search for: ", searchBox);

        for(Category category : Category.values() ) {
            categoryLB.addItem(Normalizer.capitalizeName(category.toString()));
        }

        searchBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    search();
                }
            }
        });

        searchButton.addStyleName("m-button");

        searchButton.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                search();
            }
        });

        typeLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                String type = typeLB.getValue(typeLB.getSelectedIndex()).toLowerCase();
                if(type.equals("user")) {
                    categoryEntry.setVisible(false);
                } else {
                    categoryEntry.setVisible(true);
                }
            }
        });

        categoryEntry.setVisible(false);

        formPanel.add(typeEntry);
        formPanel.add(categoryEntry);
        formPanel.add(searchEntry);
        formPanel.add(searchButton);

        main.add(formPanel);
        main.add(resultPanel);
    }

    private void search() {
        String type = typeLB.getValue(typeLB.getSelectedIndex()).toLowerCase();
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

    private Constraint getConstraint() {
        String[] tokens = parameter.split("&");
        Constraint constraint = new Constraint();
        constraint.setType(SearchType.valueOf(tokens[0].toUpperCase()));
        for(int j = 1; j < tokens.length; j++) {
            if(tokens[j].startsWith("c=")) {
                try {
                    constraint.setCategory(Category.valueOf(getValue(tokens[j]).toUpperCase()));
                } catch(IllegalArgumentException e) {
                    constraint.setCategory(Category.ALL);
                }
            } else if(tokens[j].startsWith("u=")) {
                constraint.setUsername(getValue(tokens[j]));
            } else if(tokens[j].startsWith("p=")) {
                constraint.setPictureTitle(getValue(tokens[j]));
            } else if(tokens[j].startsWith("m=")) {
                constraint.setMissionName(getValue(tokens[j]));
            }
        }
        return constraint;
    }

    private void result() {
        resultPanel.clear();
        Constraint constraint = getConstraint();
        SearchType type = constraint.getType();
        switch(type) {
        case USER:
            resultPanel.add(new UserSection(constraint));
            break;
        case PICTURE:
            resultPanel.add(new GallerySection(constraint));
            break;
        case MISSION:
            resultPanel.add(new MissionSection(constraint));
            break;
        case ALL:
            break;
        }
    }

    private String getValue(String token) {
        return token.substring(token.indexOf("=")+1);
    }

    @Override
    public Widget asWidget() {
        resultPanel.clear();
        if(parameter != null) {
            result();
        }
        onLoad();
        parameter = null;
        return main;
    }
}
