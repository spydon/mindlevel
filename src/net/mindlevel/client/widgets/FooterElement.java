package net.mindlevel.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class FooterElement extends Composite {
    public FooterElement() {
        FlowPanel p = new FlowPanel();
        p.add(new QuoteElement());
        initWidget(p);
        setStyleName("footer");
    }
}