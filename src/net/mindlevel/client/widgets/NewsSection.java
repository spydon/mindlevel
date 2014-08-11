package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.NewsService;
import net.mindlevel.client.services.NewsServiceAsync;
import net.mindlevel.shared.News;

import org.skrat.gwt.client.ui.ColorBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewsSection extends Composite {

    private final VerticalPanel p;

    private final NewsServiceAsync newsService = GWT
            .create(NewsService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public NewsSection(final int number) {
        p = new VerticalPanel();
        HTML header = new HTML("News");
        final LoadingElement l = new LoadingElement();
        header.addStyleName("news-header");
        p.add(header);
        p.add(l);

        newsService.getNews(number, new AsyncCallback<ArrayList<News>>() {

            @Override
            public void onSuccess(ArrayList<News> news) {
                if(news.size() > 0) {
                    for(News n : news) {
                        p.add(new NewsElement(n));
                    }
                }
                l.removeFromParent();
            }

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });

        final HTML colourText = new HTML("So the site probably needs a background colour, but I can't decide. If you want to try some colour combinations use the colour picker below (Random if none choosen). If you have found a good colour (the colour is saved in the link) or have other design tips please post it on the <a href='https://facebook.com/mindlvl'>facebook page</a> Cheers");
        final ColorBox picker = new ColorBox();
        final Button changeB = new Button("Change Background");
        picker.getElement().setPropertyString("placeholder", "Click to change colour!");
        changeB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String colour = picker.getText();
                if(colour.equals("") || !colour.contains("#") || colour.length()!=7) {
                    colour = getRandomColor();
                }
                RootPanel.getBodyElement().getStyle().setBackgroundColor(colour);
                History.newItem("colour="+colour.substring(1), false);
            }
        });
        p.add(colourText);
        p.add(changeB);
        p.add(picker);

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("news-section");
    }

    /**
     * generate a random hex color
     *
     * @return
     */
    public static String getRandomColor() {
        String hex1 = getRandomHex();
        String hex2 = getRandomHex();
        String hex3 = getRandomHex();
        String hex4 = getRandomHex();
        String hex5 = getRandomHex();
        String hex6 = getRandomHex();
        String color = "#" + hex1 + hex2 + hex3 + hex4 + hex5 + hex6;
        return color;
    }

    /**
     * get random hex
     *
     * @return int
     */
    private static String getRandomHex() {
        String[] hex = new String[] { "0", "1", "2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F" };
        int randomNum = Random.nextInt(hex.length);
        String sHex = hex[randomNum];
        return sHex;
    }
}