package net.mindlevel.mobile.client;

import net.mindlevel.mobile.client.widget.MindlevelImageButton;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.widget.button.image.NextitemImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.PreviousitemImageButton;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPropertyHelper.Justification;

public class MindlevelMobile {

    HistoryManager historyManager;
    private final SimplePanel appArea;
    private final static ButtonBar navigation = new ButtonBar();

    public MindlevelMobile() {
        appArea = new SimplePanel();
        historyManager = new HistoryManager(appArea);
        onModuleLoad();
    }

    public void onModuleLoad() {
        ViewPort viewPort = new MGWTSettings.ViewPort();
        viewPort.setUserScaleAble(false).setMinimumScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);

        MGWTSettings settings = new MGWTSettings();
        settings.setViewPort(viewPort);
        settings.setIconUrl("./images/logo.png");
        settings.setFullscreen(true);
//        settings.setPreventScrolling(true);

        MGWT.applySettings(settings);

        History.addValueChangeHandler(historyManager);
        initBar();

        appArea.addStyleName("m-apparea");

        RootPanel.get().add(appArea);
        RootPanel.get().add(getBar());
    }

    public static ButtonBar getBar() {
        return navigation;
    }

    private void initBar() {
        navigation.addStyleName("m-bar");
        navigation.setJustification(Justification.CENTER);
        PreviousitemImageButton previous = new PreviousitemImageButton();
        MindlevelImageButton home = new MindlevelImageButton();
        NextitemImageButton next = new NextitemImageButton();

        previous.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.back();
            }
        });

        home.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.newItem("");
            }
        });

        next.addTapHandler(new TapHandler() {
            @Override
            public void onTap(TapEvent event) {
                History.forward();
            }
        });

        navigation.add(previous);
        navigation.add(home);
        navigation.add(next);
    }

    public static void showBar() {
        getBar().removeStyleName("superhidden");
    }

    public static void hideBar() {
        getBar().addStyleName("superhidden");
    }
}
