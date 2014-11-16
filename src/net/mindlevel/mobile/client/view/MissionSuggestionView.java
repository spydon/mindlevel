package net.mindlevel.mobile.client.view;

import java.util.ArrayList;
import java.util.HashSet;

import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Mission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MTextArea;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.input.checkbox.MCheckBox;
import com.googlecode.mgwt.ui.client.widget.input.listbox.MListBox;

public class MissionSuggestionView extends MPage {
    protected VerticalPanel main;
    private VerticalPanel formPanel;
    private Label errorLabel;
    private MTextBox title;
    private MTextArea description;
    private MCheckBox adult;
    private Button upload;

    private ArrayList<CategoryLine> categoryList;

    private final MissionServiceAsync missionService = GWT
            .create(MissionService.class);

    public MissionSuggestionView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    private void init() {
        main.clear();
        categoryList = new ArrayList<CategoryLine>();

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        errorLabel = new Label("");

        formPanel = new VerticalPanel();
        formPanel.addStyleName("m-button-panel");

        title = new MTextBox();
        description = new MTextArea();
        adult = new MCheckBox();
        adult.setValue(false);
        upload = new Button("Upload for validation");

        FormEntry titleEntry = new FormEntry("Title: ", title);
        FormEntry descriptionEntry = new FormEntry("Description: ", description);
        FormEntry adultEntry = new FormEntry("Adult/NSFW?", adult);

        errorLabel.addStyleName("m-margin");

        upload.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                missionUpload();
            }
        });

        formPanel.add(titleEntry);
        formPanel.add(descriptionEntry);
        formPanel.add(adultEntry);
        formPanel.add(new CategoryLine());

        main.add(logo);
        main.add(formPanel);
        main.add(errorLabel);
        main.add(upload);
    }

    private void missionUpload() {
        final Mission mission = new Mission(title.getText(), getCategories(), description.getText(), UserTools.getUsername(), adult.getValue(), false);

        missionService.uploadMission(mission, UserTools.getToken(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }

            @Override
            public void onSuccess(Void noreturn) {
                History.newItem("");
                HandyTools.showDialogBox("Success", new HTML("<h1>Successfully suggested a mission.</h1>"));
            }
        });
    }

    private HashSet<Category> getCategories() {
        HashSet<Category> categories = new HashSet<Category>();
        for(CategoryLine f : categoryList) {
            categories.add(f.getCategory());
        }
        return categories;
    }

    protected class CategoryLine extends FormEntry {
        private final HorizontalPanel main;
        final MListBox category = new MListBox();
        final Button button = new Button();

        public CategoryLine() {
            button.addStyleName("m-add-button");

            for(Category c : Category.values()) {
                if(c != Category.ALL) {
                    category.addItem(c.toString());
                }
            }

            button.addTapHandler(new TapHandler() {
                @Override
                public void onTap(TapEvent event) {
                    if(category.isEnabled()) {
                        addLine();
                    } else {
                        removeLine();
                    }
                }
            });

            category.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent arg0) {
//                    category.setEnabled(false);
//                    addLine();
                }
            });

            main = new HorizontalPanel();
            main.add(category);
            main.add(button);
            setWidget("Category", main);
            categoryList.add(this);
        }

        public Category getCategory() {
            return Category.valueOf(category.getValue(category.getSelectedIndex()).toUpperCase());
        }

        private void addLine() {
            category.setEnabled(false);
            button.removeStyleName("m-add-button");
            button.addStyleName("m-remove-button");
            CategoryLine categoryLine = new CategoryLine();
            formPanel.add(categoryLine);
        }

        private void removeLine() {
            if(categoryList.size() == 1) {
                category.setEnabled(true);
                category.setSelectedIndex(0);
                button.removeStyleName("m-remove-button");
                button.addStyleName("m-add-button");
            } else {
                categoryList.remove(this);
                removeFromParent();
            }
        }
    }

    @Override
    public Widget asWidget() {
        onLoad();
        if(UserTools.isLoggedIn()) {
            init();
        } else {
            HandyTools.notLoggedInBox();
        }
        return main;
    }
}
