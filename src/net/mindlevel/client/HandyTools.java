package net.mindlevel.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HandyTools {

    public static void showDialogBox(String title, HTML text) {
        Mindlevel.forceFocus = false;
        final DialogBox db = new DialogBox();
        db.setText(title);
        final Button closeButton = new Button("Ok");
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                db.hide();
            }
        });
        VerticalPanel dbPanel = new VerticalPanel();
        text.addStyleName("dialogBoxHTML");
        dbPanel.add(text);
        dbPanel.add(closeButton);
        db.add(dbPanel);
        db.center();
        closeButton.setFocus(true);
    }

    public static void notLoggedInBox() {
        HandyTools.showDialogBox("Error", new HTML("You must be logged in to comment<br /><a href=\"#login\">Login</a> or <a href=\"#register\">Register</a>"));
    }


    //TODO: This one is too confusing, rewrite!
    public static void setRightView(boolean logIn, String username) {
        RootPanel.get("hidelogin").setStyleName("superhidden", logIn);
        RootPanel.get("hidelogout").setStyleName("superhidden", !logIn);
        RootPanel.get("hideregister").setStyleName("superhidden", logIn);
        RootPanel.get("profile").getElement().setInnerHTML(username);
        RootPanel.get("hideprofile").setStyleName("superhidden", !logIn);
        RootPanel.get("hidechat").setStyleName("superhidden", !logIn);
        RootPanel.get("adminmenu").setStyleName("superhidden", !logIn && !UserTools.isAdmin());
        RootPanel.get("apparea").setStyleName("adminbar", logIn && UserTools.isAdmin());
    }

    public static void debugMsg(String msg) {
        Element debug = RootPanel.get("debug").getElement();
        debug.setInnerHTML(debug.getInnerHTML() + "</br>" + msg);
    }

    //Unix time to a readable date
    public static String unixToDate(long unixtime) {
        java.util.Date date = new java.util.Date(unixtime*1000);
        DefaultDateTimeFormatInfo info = new DefaultDateTimeFormatInfo();
        DateTimeFormat dtf = new DateTimeFormat("EEE, d MMM yyyy HH:mm:ss", info) {};
        return dtf.format(date);
    }

    public static String getCategoryAnchors(ArrayList<String> categories) {
        String categoryAnchors = "";
        for(String category : categories)
            if(categories.indexOf(category) != 0)
                categoryAnchors += ", " + getAnchor("category", category, category);
            else
                categoryAnchors = getAnchor("category", category, category);
        return categoryAnchors;
    }

    public static String getAnchor(String type, String data, String name) {
        return data!=null ? "<a href=#"+type+"="+data+">"+name+"</a>" : "";
    }
}
