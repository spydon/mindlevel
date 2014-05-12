package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.News;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NewsElement extends Composite {

    private final VerticalPanel backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public NewsElement(News news) {
        backPanel = new VerticalPanel();
        Label timestamp = new Label(HandyTools.formatDate(news.getTimestamp()).toString());
        HTML username = new HTML("Author: " + HandyTools.getAnchor("user", news.getUsername(), news.getUsername()));
        HTML content = HandyTools.formatHtml(news.getContent());
        timestamp.addStyleName("news-timestamp");
        username.addStyleName("news-author");
        content.addStyleName("news-content");

        backPanel.add(timestamp);
        backPanel.add(content);
        backPanel.add(username);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("news-element");
    }
}