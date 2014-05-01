package net.mindlevel.client.pages.dialog;


import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.ReportService;
import net.mindlevel.client.services.ReportServiceAsync;
import net.mindlevel.shared.Report;
import net.mindlevel.shared.UserTools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class ReportBox {
    private final DialogBox popup;
    private final ListBox typeLB = new ListBox();
    private final TextBox urlBox = new TextBox();
    private final TextArea textArea = new TextArea();

    /**
     * Create a remote service proxy to talk to the server-side report
     * service.
     */
    private final ReportServiceAsync reportService = GWT
            .create(ReportService.class);


    public ReportBox() {
        popup = new DialogBox(true);
        init();
    }

    private void init() {
        FlexTable t = new FlexTable();
        Label typeL = new Label("Type: ");
        typeLB.addItem("Bug");
        typeLB.addItem("Abuse");
        typeLB.addItem("Feature request");
        typeLB.addStyleName("fullwidth");

        urlBox.addStyleName("search-box");
        urlBox.getElement().setPropertyString("placeholder", "Concerning URL(optional)");
        textArea.getElement().setPropertyString("placeholder", "What is on your mind?");
        textArea.addStyleName("report-text-area");

        Button reportButton = new Button("Report");
        Button cancelButton = new Button("Cancel");
        reportButton.addStyleName("fullwidth");
        cancelButton.addStyleName("fullwidth");
        cancelButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                popup.hide();
            }
        });

        reportButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                popup.hide();
                report();
            }
        });

        t.setWidget(0, 0, typeL);
        t.setWidget(0, 1, typeLB);
        t.setWidget(2, 0, urlBox);
        t.setWidget(3, 0, textArea);
        t.setWidget(4, 0, reportButton);
        t.setWidget(5, 0, cancelButton);
        t.getFlexCellFormatter().setColSpan(2, 0, 2);
        t.getFlexCellFormatter().setColSpan(3, 0, 2);
        t.getFlexCellFormatter().setColSpan(4, 0, 2);
        t.getFlexCellFormatter().setColSpan(5, 0, 2);

        popup.setText("Report");
        popup.add(t);
        popup.center();
        popup.show();
    }

    private void report() {
        String type = typeLB.getValue(typeLB.getSelectedIndex()).toLowerCase();
        String url = urlBox.getText();
        String content = textArea.getText();
        Report report = new Report(UserTools.getUsername(), type, url, content);
        reportService.addReport(report, UserTools.getToken(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                HandyTools.showDialogBox("Successful", new HTML("Report successfully sent, thank you for making mindlevel better!"));
            }

            @Override
            public void onFailure(Throwable arg0) {
                HandyTools.showDialogBox("Error", new HTML("Something went wrong, if the problem persists please contact <a href=\"#user=spydon\">spydon</a>!"));
            }
        });
    }
}
