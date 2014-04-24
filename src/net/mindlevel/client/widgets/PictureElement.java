package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PictureElement extends Composite {

    private final VerticalPanel backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public PictureElement(final MetaImage metaImage) {
        backPanel = new VerticalPanel();
        Image image = new Image("pictures/" + metaImage.getFilename());
        image.addStyleName("last-picture");
        image.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                History.newItem("picture=" + metaImage.getId());
            }
        });
        HTML title = new HTML("Title: " + HandyTools.getAnchor("picture", "" + metaImage.getId(), metaImage.getTitle()));
        HTML tags = new HTML();
        for(String tag : metaImage.getTags()) {
            if(!tags.getHTML().equals("")) {
                tags.setHTML(tags.getHTML() + ", ");
            } else {
                tags.setHTML("People: ");
            }
            tags.setHTML(tags.getHTML() + HandyTools.getAnchor("user", tag, tag));
        }

        backPanel.add(image);
        backPanel.add(title);
        backPanel.add(tags);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("last-picture-element");
    }
}