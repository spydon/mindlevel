package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.NewsService;
import net.mindlevel.client.services.NewsServiceAsync;
import net.mindlevel.shared.News;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
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
        header.addStyleName("news-header");
        p.add(header);

        newsService.getNews(number, new AsyncCallback<ArrayList<News>>() {

            @Override
            public void onSuccess(ArrayList<News> news) {
                if(news.size() > 0) {
                    for(News n : news) {
                        p.add(new NewsElement(n));
                    }
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("news-section");
    }
}