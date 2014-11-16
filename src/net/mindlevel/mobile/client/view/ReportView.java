package net.mindlevel.mobile.client.view;

import net.mindlevel.client.services.ReportService;
import net.mindlevel.client.services.ReportServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.Report;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.image.ChatImageButton;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MTextArea;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.input.listbox.MListBox;

public class ReportView extends MPage {
    protected VerticalPanel main;
    private final MListBox typeLB = new MListBox();
    private final MTextBox urlBox = new MTextBox();
    private final MTextArea textArea = new MTextArea();

    private final ChatImageButton reportButton;

    /**
     * Create a remote service proxy to talk to the server-side report
     * service.
     */
    private final ReportServiceAsync reportService = GWT
            .create(ReportService.class);

    public ReportView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");

        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        VerticalPanel formPanel = new VerticalPanel();
        formPanel.addStyleName("m-button-panel");

        typeLB.addItem("Bug");
        typeLB.addItem("Abuse");
        typeLB.addItem("Feature request");

        urlBox.setPlaceHolder("Concerning URL");
        textArea.setPlaceHolder("What is on your mind?");
        FormEntry typeEntry = new FormEntry("Type:", typeLB);
        FormEntry urlEntry = new FormEntry("URL (Optional):", urlBox);
        FormEntry textEntry = new FormEntry("Report:", textArea);

        reportButton = new ChatImageButton();
        reportButton.setText("Report");
        reportButton.addStyleName("m-button");
        reportButton.addStyleName("m-long-button");
        reportButton.setWidth("300px");

        reportButton.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                report();
            }
        });

        formPanel.add(typeEntry);
        formPanel.add(urlEntry);
        formPanel.add(textEntry);
        formPanel.add(reportButton);

        main.add(logo);
        main.add(formPanel);
    }

    private void report() {
        reportButton.setVisible(false);
        final LoadingElement l = new LoadingElement();
        main.add(l);
        String type = typeLB.getValue(typeLB.getSelectedIndex()).toLowerCase();
        String url = urlBox.getText();
        String content = textArea.getText();
        Report report = new Report(UserTools.getUsername(), type, url, content);
        reportService.addReport(report, UserTools.getToken(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                l.removeFromParent();
                History.newItem("");
                HandyTools.showDialogBox("Successful", new HTML("Report successfully sent, thank you for making mindlevel better!"));
            }

            @Override
            public void onFailure(Throwable arg0) {
                l.removeFromParent();
                reportButton.setVisible(true);
                HandyTools.showDialogBox("Error", new HTML("Something went wrong, if the problem persists please contact <a href=\"#user=spydon\">spydon</a>!"));
            }
        });
    }

    @Override
    public Widget asWidget() {
        onLoad();
        return main;
    }
}
