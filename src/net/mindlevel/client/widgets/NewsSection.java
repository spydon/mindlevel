package net.mindlevel.client.widgets;

import java.util.ArrayList;

import net.mindlevel.client.services.NewsService;
import net.mindlevel.client.services.NewsServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.shared.News;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

public class NewsSection extends Composite {

    private final FlowPanel p;
    private final ArrayList<News> news;

    private final NewsServiceAsync newsService = GWT
            .create(NewsService.class);

    /**
     * Constructs a CommentSection that controls a number of ReadBox and WriteBox
     *
     */
    public NewsSection(final int number) {
        p = new FlowPanel();
        news = new ArrayList<>();
        HTML header = new HTML("<h2>Welcome to MindLevel</h2>");
        final LoadingElement l = new LoadingElement();
        final SimplePanel currentNews = new SimplePanel();
        currentNews.add(l);
        header.addStyleName("news-header");
        p.add(header);
        p.add(currentNews);
        pollNews(currentNews, number);

        // All composites must call initWidget() in their constructors.
        initWidget(p);

        // Give the overall composite a style name.
        setStyleName("news-section");
    }

    private void pollNews(final SimplePanel currentNews, int number) {
        newsService.getNews(number, new AsyncCallback<ArrayList<News>>() {

            @Override
            public void onSuccess(ArrayList<News> news) {
                currentNews.clear();
                if(news.size() > 0 && getNews().size() == 0) {
                    for(News n : news) {
                        getNews().add(n);
                    }
                    currentNews.setWidget(new NewsElement(news.get(0)));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                currentNews.clear();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });
    }

    protected ArrayList<News> getNews() {
        return news;
    }
}