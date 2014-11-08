package net.mindlevel.client.widgets;

import net.mindlevel.client.HandyTools;
import net.mindlevel.shared.Category;
import net.mindlevel.shared.Mission;
import net.mindlevel.shared.Normalizer;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;

public class MissionElement extends Composite
implements HasClickHandlers {

    private final Panel backPanel;

    /**
     * Constructs an MissionElement with the given news displayed.
     *
     * @param mission the mission to graphically represent
     */
    public MissionElement(final Mission mission) {
        ClickHandler handler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                AnchorElement eventTarget = (AnchorElement)event.getNativeEvent().getEventTarget().cast();
                if(eventTarget == null || !eventTarget.getTagName().equals("A")) {
                    String constraint = mission.isValidated() ? "" : "&validated=false";
                    History.newItem("mission="+mission.getId()+constraint);
                }
            }
        };

        HTML missionName = new HTML("<h3>" + HandyTools.formatHtml(mission.getName()) + "</h3>");
        backPanel = new FlowPanel();

        HTML categories = new HTML();
        for(Category category : mission.getCategories()) {
            if(!categories.getHTML().equals("")) {
                categories.setHTML(categories.getHTML() + ", ");
            } else {
                categories.setHTML("Categories: ");
            }
            String categoryName = Normalizer.capitalizeName(category.toString());
            categories.setHTML(categories.getHTML() + HandyTools.getAnchor("search&type=mission&c", categoryName.toLowerCase(), categoryName));
        }
        backPanel.add(missionName);
        backPanel.add(new HTML("Creator: " + HandyTools.getAnchor("user", mission.getCreator(), mission.getCreator())));
        backPanel.add(categories);
        backPanel.add(new HTML("Created: " + HandyTools.formatDate(mission.getCreated())));

        addClickHandler(handler);

        // All composites must call initWidget() in their constructors.
        initWidget(backPanel);

        // Give the overall composite a style name.
        setStyleName("mission-element");
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}