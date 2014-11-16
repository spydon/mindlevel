package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.tools.HtmlTools;
import net.mindlevel.shared.MetaImage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

public class PictureElement extends Composite {

    private final FlowPanel backPanel;

    /**
     * Constructs an NewsElement with the given news displayed.
     *
     * @param caption the caption to be displayed with the check box
     */
    public PictureElement(final MetaImage metaImage, boolean isSimple) {
        backPanel = new FlowPanel();

        Image image = new Image(Mindlevel.PATH + "pictures/" + metaImage.getThumbnail());
        image.addStyleName("last-picture");
        image.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                History.newItem("picture=" + metaImage.getId());
            }
        });

        SimplePanel imageWrapper = new SimplePanel();
        imageWrapper.addStyleName("last-picture-wrapper");
        imageWrapper.setWidget(image);

        HTML title = new HTML(HtmlTools.getAnchor("picture", "" + metaImage.getId(),
                HtmlTools.formatHtml(metaImage.getTitle()).toString()));
        HTML mission = new HTML(HtmlTools.concat("<b>Mission:</b> ", HtmlTools.getAnchor("mission", "" + metaImage.getMission().getId(),
                HtmlTools.formatHtml(metaImage.getMission().getName()).toString())));

        title.addStyleName("text-center");

        backPanel.add(imageWrapper);
        backPanel.add(title);
        if(!isSimple) {
            backPanel.add(mission);
            backPanel.add(new HTML(HtmlTools.getCategoryAnchors(metaImage.getCategories())));
        }
//        backPanel.add(new HTML(HtmlTools.buildTagHTML(metaImage.getTags())));
        backPanel.add(new UserTagSection(metaImage.getTags(), true, true, UserTagElement.SIZE.SMALL));

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);
        // Give the overall composite a style name.
        setStyleName("picture-element");
    }
}