package net.mindlevel.client.tools;

import static com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape;

import java.util.HashSet;

import net.mindlevel.shared.Category;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;

public class HtmlTools {
    public static native Element activeElement() /*-{
        return $doc.activeElement;
    }-*/;

    public static void scrollDown() {
        Window.scrollTo(0, Window.getScrollTop()+200);
    }

    public static SafeHtml getCategoryAnchors(HashSet<Category> categories) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        for (Category categoryObj : categories) {
            String category = categoryObj.toString();
            if (builder.toSafeHtml().asString().equals("")) {
              builder.append(getAnchor("search&type=picture&c", category.toLowerCase(), category));
            } else {
              builder.appendHtmlConstant(", ").append(getAnchor("search&type=picture&c", category.toLowerCase(), category));
            }
        }
        return builder.toSafeHtml();
    }

    public static SafeHtml getAnchor(String type, String data, String name, boolean validated) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        String uriData = SafeHtmlUtils.htmlEscape(data);
        if (!validated) {
            builder.appendHtmlConstant("<a href='#" + type + "=" + uriData + "&validated=false'>");
        } else {
            builder.appendHtmlConstant("<a href='#" + type + "=" + uriData + "'>");
        }
        builder.appendEscaped(name).appendHtmlConstant("</a>");
        return builder.toSafeHtml();
    }

    public static SafeHtml getAnchor(String type, String data, String name) {
        return getAnchor(type, data, name, true);
    }

    public static SafeHtml buildTagHTML(HashSet<String> tags) {
        String separator = ",&nbsp;";
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant("<b>Tags: </b>");
        if(tags != null && !tags.isEmpty()) {
            int x = 0;
            for(String tag : tags) {
                builder.append(HtmlTools.getAnchor("user", tag, tag));
                if (x != tags.size()-1) {
                    builder.appendHtmlConstant(separator);
                }
                x++;
            }
        }
        return builder.toSafeHtml();
    }

    public static String formatHtml(String text) {
        return htmlEscape(text).replaceAll("\n", "<br>");
    }

    public static SafeHtml concat(String constant, SafeHtml safeHtml) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant(constant);
        builder.append(safeHtml);
        return builder.toSafeHtml();
    }
}