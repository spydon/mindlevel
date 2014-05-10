package net.mindlevel.client.pages;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Tutorial {
    private final RootPanel appArea;

    public Tutorial(RootPanel appArea) {
        this.appArea = appArea;
        init();
    }

    private void init() {
        HTML terms = new HTML(
                "<h1>How to use MindLevel</h1> "
              + "<h2>Home</h2>"
              + "<p>The home tab contains four sections: last finished missions, news, last logins and newest users. "
              + "They should be quite self explanatory and everything except the news elements is clickable.</p> "
              + "<h2>Missions</h2>"
              + "<p>Under this tab you can see the available missions, if you click on a mission you see more information about it. "
              + "You can also suggest missions here, if your mission gets accepted by an admin/moderator you get 100 points "
              + "and it will become visible on the site.</p>"
              + "<p>If you clicked on a mission you can upload a finished mission and get points for it. In the future you will get "
              + "badges and special rights the more points you have. </p>"
              + "<h2>Pictures</h2>"
              + "<p>Here you can see all the finished missions by the users of the site. You can navigate by pressing the arrows "
              + "or press the picture to get a random picture. You can also press 'H' to see the keyboard navigation available. "
              + "You can navigate with the arrow keys and press 'R' for a random picture. </p>"
              + "<p>To vote you press the up and down buttons, it costs 1 point to give an upvote and that gives the tagged users "
              + "of the picture 10 points each. It costs 10 points to downvote and that gives all the tagged users 10 minus point, "
              + "so only give out minus points if you really dislike the picture.</p>"
              + "<p>Write in the comment field to comment, or press the reply button on a comment if you want to reply. You can "
              + "delete your comment by pressing the delete button after pressing the edit button."
              + "<h2>Highscore</h2>"
              + "<p>Here you can see the honorable users with the most score. You can press them to get more information about them.</p>"
              + "<h2>Chat</h2> "
              + "<p>This chat is connected to irc.freenode.net on the channel #mindlevel, if you want to use your own IRC-client. "
              + "<b>Beware</b> that everybody might not be who they claim to be!</p> "
              + "<h2>Misc</h2> "
              + "<p>If you have any further questions or concerns, or if you encounter difficulties while "
              + "using the site, please contact info@mindlevel.net or file a bug, abuse or "
              + "feature request at <a href=\"#report\">#report</a></p>."
              + "<p>Hope you have a great time here at Mindlevel! /<a href=\"#user=spydon\">spydon</a> </p>");
        terms.addStyleName("terms");
        appArea.add(terms);
    }
}
