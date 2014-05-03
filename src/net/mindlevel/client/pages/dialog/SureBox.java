package net.mindlevel.client.pages.dialog;


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SureBox {
    private final DialogBox popup;
    private final Button yes, no;

    public SureBox() {
        popup = new DialogBox(true);
        yes = new Button("Yes");
        no = new Button("No");
        yes.addStyleName("fullwidth");
        no.addStyleName("fullwidth");
        init();
    }

    public SureBox(ClickHandler yesHandler, ClickHandler noHandler) {
        popup = new DialogBox(true);
        yes = new Button();
        no = new Button();
        addYesHandler(yesHandler);
        addNoHandler(noHandler);
        init();
    }

    private void init() {
        VerticalPanel backPanel = new VerticalPanel();
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.addStyleName("fullwidth");
        buttonPanel.add(yes);
        buttonPanel.add(no);
        backPanel.add(new HTML("Are you sure you want to do this?"));
        backPanel.add(buttonPanel);
        popup.setAutoHideEnabled(true);
        popup.add(backPanel);
        popup.setText("Sure?");
    }

    public void show() {
        popup.center();
        popup.show();
    }

    public void hide() {
        popup.hide();
    }

    public void addYesHandler(ClickHandler clickHandler) {
        yes.addClickHandler(clickHandler);
    }

    public void addNoHandler(ClickHandler clickHandler) {
        no.addClickHandler(clickHandler);
    }
}
