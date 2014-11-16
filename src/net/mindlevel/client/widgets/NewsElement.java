package net.mindlevel.client.widgets;

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
//        HTML username = new HTML("Author: " + HandyTools.getAnchor("user", news.getUsername(), news.getUsername()).asString());
        UserTagElement userTag = new UserTagElement(news.getUsername(), true, true, UserTagElement.SIZE.SMALL);
        HTML content = new HTML(news.getContent());
        userTag.addStyleName("news-author");
        content.addStyleName("news-content");

        backPanel.add(content);
        backPanel.add(userTag);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("news-element");
    }
}