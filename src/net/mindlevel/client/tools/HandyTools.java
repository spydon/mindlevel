package net.mindlevel.client.tools;

import java.util.Date;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.client.widgets.UserTagElement;
import net.mindlevel.shared.User;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.ui.client.widget.dialog.AlertDialog;

public class HandyTools {

    private static VerticalPanel loadingPanel;
    private static UserTagElement userTag;

    public static void initTools() {
        loadingPanel = new VerticalPanel();
        loadingPanel.setStylePrimaryName("loading-panel");
        loadingPanel.setVisible(false);
        loadingPanel.add(new LoadingElement());
        RootPanel.get().add(loadingPanel);
    }

    public static void showDialogBox(String title, HTML text) {
        if(Mindlevel.isDesktop()) {
            showDesktopDialogBox(title, text);
        } else {
            showMobileDialogBox(title, text);
        }
    }

    private static void showDesktopDialogBox(String title, HTML text) {
        final DialogBox db = new DialogBox(true);
        db.setText(title);
        final Button closeButton = new Button("Ok");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                db.hide();
            }
        });
        VerticalPanel dbPanel = new VerticalPanel();
        text.addStyleName("dialog-box-html");
        dbPanel.add(text);
        dbPanel.add(closeButton);
        db.add(dbPanel);
        db.center();
        closeButton.setFocus(true);
        text.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                db.hide();
            }
        });
    }

    private static void showMobileDialogBox(String title, HTML text) {
        final AlertDialog alert = new AlertDialog(title, "");
        alert.setHTML(text.getHTML());
        alert.show();
    }

    public static void showDialogBox(String title, String text) {
        showDialogBox(title, new HTML(text));
    }

    public static void setLoading(boolean isLoading) {
        loadingPanel.setVisible(isLoading);
    }

    public static void notLoggedInBox() {
        String lastPage = History.getToken();
        HandyTools.showDialogBox("Error", new HTML("You must be logged in to do that <br /><a href=\"#login&session=" + lastPage + "\">Login</a> or <a href=\"#register&session=" + lastPage + "\">Register</a>"));
    }

    public static void setRightView(boolean logIn, User user) {
        if(Mindlevel.isDesktop()) {
            if(logIn) {
                Document.get().getElementById("hidelogin").addClassName("superhidden");
//                Document.get().getElementById("hidelogout").removeClassName("superhidden");
                Document.get().getElementById("hideregister").addClassName("superhidden");
//                Document.get().getElementById("profile").setInnerHTML(username);
                if (userTag == null) {
                    userTag = new UserTagElement(user, true, false);
                    userTag.addStyleName("user-tag-profile");
                }
                RootPanel.get().add(userTag);

//                Document.get().getElementById("hideprofile").removeClassName("superhidden");
                Document.get().getElementById("hidechat").removeClassName("superhidden");
            } else {
                if (userTag != null) {
                    userTag.removeFromParent();
                    userTag = null;
                }
                Document.get().getElementById("hidelogin").removeClassName("superhidden");
//                Document.get().getElementById("hidelogout").addClassName("superhidden");
                Document.get().getElementById("hideregister").removeClassName("superhidden");
//                Document.get().getElementById("hideprofile").addClassName("superhidden");
                Document.get().getElementById("hidechat").addClassName("superhidden");
            }

            if(UserTools.isAdmin()) {
                Document.get().getElementById("admin-menu").removeClassName("superhidden");
                Document.get().getElementById("apparea").addClassName("adminbar");
            } else {
                Document.get().getElementById("admin-menu").addClassName("superhidden");
                Document.get().getElementById("apparea").removeClassName("adminbar");
            }
        }
    }

    public static void debugMsg(String msg) {
        Element debug = RootPanel.get("debug").getElement();
        debug.setInnerHTML(debug.getInnerHTML() + "</br>" + msg);
    }

    //Unix time to a readable date
    public static Date unixToDate(long unixTime) {
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dtf = new DateTimeFormat("EEE, d MMM yyyy HH:mm:ss", info) {};
        String converted = dtf.format(new Date(unixTime*1000));
        return dtf.parse(converted);
    }

    public static String formatDate(Date timestamp) {
        DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
        return dtf.format(timestamp);
    }

    public static String formatOnlyDate(Date timestamp) {
        DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
        return dtf.format(timestamp);
    }
}