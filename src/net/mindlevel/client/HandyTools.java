package net.mindlevel.client;

import static com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape;

import java.util.ArrayList;
import java.util.Date;

import net.mindlevel.client.widgets.LoadingElement;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Normalizer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HandyTools {

    private static VerticalPanel loadingPanel;

    public static void initTools() {
        loadingPanel = new VerticalPanel();
        loadingPanel.setStylePrimaryName("loading-panel");
        loadingPanel.setVisible(false);
        loadingPanel.add(new LoadingElement());
        RootPanel.get().add(loadingPanel);
    }

    public static native Element activeElement() /*-{
        return $doc.activeElement;
    }-*/;

    public static void scrollDown() {
        Window.scrollTo(0, Window.getScrollTop()+200);
    }

    public static void showDialogBox(String title, HTML text) {
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
        text.addStyleName("dialogBoxHTML");
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


    public static void setRightView(boolean logIn, String username) {
        if(logIn) {
            Document.get().getElementById("hidelogin").addClassName("superhidden");
            Document.get().getElementById("hidelogout").removeClassName("superhidden");
            Document.get().getElementById("hideregister").addClassName("superhidden");
            Document.get().getElementById("profile").setInnerHTML(username);
            Document.get().getElementById("hideprofile").removeClassName("superhidden");
            Document.get().getElementById("hidechat").removeClassName("superhidden");
        } else {
            Document.get().getElementById("hidelogin").removeClassName("superhidden");
            Document.get().getElementById("hidelogout").addClassName("superhidden");
            Document.get().getElementById("hideregister").removeClassName("superhidden");
            Document.get().getElementById("hideprofile").addClassName("superhidden");
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

    public static String getCategoryAnchors(ArrayList<Category> categories) {
        String categoryAnchors = "";
        for(Category categoryObj : categories) {
            String category = categoryObj.toString().toLowerCase();
            if(categories.indexOf(categoryObj) != 0) {
                categoryAnchors += ", " + getAnchor("search&type=picture&c", category, Normalizer.capitalizeName(category));
            } else {
                categoryAnchors = getAnchor("search&type=picture&c", category, Normalizer.capitalizeName(category));
            }
        }
        return categoryAnchors;
    }

    public static String getAnchor(String type, String data, String name) {
        return data!=null ? "<a href=#"+type+"="+data+">"+name+"</a>" : "";
    }

    public static String formatHtml(String text) {
        return htmlEscape(text).replaceAll("\n", "<br>");
    }
}
