package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.News;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class NewsElement extends Composite {

    private final FlowPanel backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public NewsElement(News news) {
        backPanel = new FlowPanel();
//        Label timestamp = new Label(HandyTools.formatOnlyDate(news.getTimestamp()).toString());
        HTML username = new HTML("Author: " + HandyTools.getAnchor("user", news.getUsername(), news.getUsername()).asString());
        HTML content = new HTML(news.getContent());
//        timestamp.addStyleName("news-timestamp");
        username.addStyleName("news-author");
        content.addStyleName("news-content");

        backPanel.add(content);
//        backPanel.add(timestamp);
        backPanel.add(username);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("news-element");
    }
}