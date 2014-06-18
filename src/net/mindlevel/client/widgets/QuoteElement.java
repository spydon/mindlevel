package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.services.QuoteService;
import net.mindlevel.client.services.QuoteServiceAsync;
import net.mindlevel.shared.Quote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

public class QuoteElement extends Composite {

    private final SimplePanel p;

    private final QuoteServiceAsync quoteService = GWT
            .create(QuoteService.class);

    public QuoteElement() {
        p = new SimplePanel();
        final LoadingElement l = new LoadingElement();
        p.add(l);

        quoteService.getQuote(new AsyncCallback<Quote>() {

            @Override
            public void onSuccess(Quote quote) {
                l.removeFromParent();
                p.add(new Label(quote.getQuote() + " - " + quote.getUsername()));
            }

            @Override
            public void onFailure(Throwable caught) {
                l.removeFromParent();
                caught.printStackTrace();
                HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
            }
        });

        // All composites must call initWidget() in their constructors.
        initWidget(p);
    }
}