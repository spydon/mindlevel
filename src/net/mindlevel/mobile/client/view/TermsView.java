package net.mindlevel.mobile.client.view;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TermsView extends MPage {
    protected VerticalPanel main;

    private boolean initialized = false;

    public TermsView() {
        main = new VerticalPanel();
        main.addStyleName("m-center");
        main.addStyleName("m-margin");
    }

    public void init() {
        initialized = true;
        Image logo = new Image("./images/logo.png");
        logo.addStyleName("m-logo");

        HTML terms = new HTML(
                "<h1>Terms and Conditions for the usage of MindLevel</h1> "
              + "<h2>Privacy</h2>"
              + "<p>At certain points in the mindlevel.net website navigation, you may be asked to share your email "
              + "address or other personal identifying information with us. As provided in these Terms and "
              + "Conditions, such information will never be distributed to a third party and your email will "
              + "never be publicly visible without your consent.<br /> "
              + "Your email address will only be used to send you the mindlevel.net newsletter and to alert you "
              + "of any information that you have specifically requested to be notified about.</p> "
              + "<h2>Use of MindLevel.net</h2>"
              + "<p>The MindLevel.net website hosts an image publication service which is equipped with "
              + "commenting facilities. While we invite you to share your opinions and questions in this way, they "
              + "must not be used to distribute spam messages, post commercial advertisements, or spread "
              + "links to illegal, malicious or dangerous websites. We do retain the right to moderate any image, comment or "
              + "written content submitted to the mindlevel.net website and to remove any content we deem to "
              + "have violated our policies.</p> "
              + "<h2>Disclaimer</h2>"
              + "<p>All of the content contained on the MindLevel.net is checked, and verified for accuracy as "
              + "much as it is possible to do so. However, we cannot guarantee either its accuracy or the safety "
              + "of any external links it might contain. Mindlevel.net, as well as its owners, affiliates, and "
              + "contributing authors can therefore not be held responsible for any problems or damage that "
              + "occurs as a result of making use of material contained on our site.</p>"
              + "<h2>Copyright</h2>"
              + "<p>Any and all of the content presented on the MindLevel.net website is, unless explicitly stated "
              + "otherwise, subject to a copyright held by the original copyright holder of the content. The uploader "
              + "is responsible for only uploading works they hold the copyright of or have the written permission "
              + "from the copyright holder of.</p>"
              + "<p>It is permissible to link to content from this site as long as the original source is clearly stated, "
              + "but the wholesale reproduction or partial modification of content is not permitted. Exceptions "
              + "are granted only if you receive prior written consent from either MindLevel.net or the uploader "
              + "of the content.</p>"
              + "<h2>Contacts</h2> "
              + "<p>Should you have any further questions or concerns about these Terms and Conditions, "
              + "or should you encounter difficulties while navigating and using the site, please contact "
              + "info@mindlevel.net</p> ");
        terms.addStyleName("m-about");
        terms.addStyleName("terms");

        main.add(logo);
        main.add(terms);
//        main.add(new HTML("(Click on the table to get more information about a user)"));
    }

    @Override
    public Widget asWidget() {
        if(!initialized) {
            init();
        }
        return main;
    }

    @Override
    public void setId(int id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
    }
}