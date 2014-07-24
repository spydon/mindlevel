package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadingElement extends Composite {

    private final VerticalPanel loadingPanel;
    public static String loadingPath = Mindlevel.PATH + "images/loading.gif";;

    /**
     * Constructs an panel with a loading gif attached to it
     *
     */
    public LoadingElement() {
        loadingPanel = new VerticalPanel();
        HTML loadingText = new HTML("Loading");
        loadingText.addStyleName("loading-text");
        Image loadingImage = new Image(loadingPath);
        loadingPanel.add(loadingImage);
        loadingPanel.add(loadingText);

        // All composites must call initWidget() in their constructors.
        initWidget(loadingPanel);
        setStyleName("loading-element");
    }
}