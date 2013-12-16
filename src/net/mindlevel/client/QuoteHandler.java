package net.mindlevel.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Random;

public class QuoteHandler {
	private final String[] quotes = 
		{"Time to do something exciting!",
		 "You didn't come here to be boring, did you?",
		 "Adrenaline time!",
		 "Freedom.",
		 "I'm bored!",
		 "You crazy hippie!",
		 "Procrastinating, are we?",
		 "The world needs you.",
		 "Do I even exist?",
		 "Knock, knock.",
		 "This is ridiculous.",
		 "Attention please!",
		 "Lets do it! Together.",
		 "Don't become a slave of the system",
		 "Don't forget to live",
		 "Not that anybody cares...",
		 "Woah, you're handsome!",
		 "I mean, WHO are you?"};
	public QuoteHandler(RootPanel quoteID) {
		quoteID.add(new Label(quotes[Random.nextInt(quotes.length)]));
	}
}
