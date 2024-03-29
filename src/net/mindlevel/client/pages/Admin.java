package net.mindlevel.client.pages;

import net.mindlevel.client.pages.dialog.BanBox;
import net.mindlevel.client.tools.UserTools;
import net.mindlevel.shared.Constraint;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Admin extends Page {

    public Admin() {
        super();
        init();
    }

    @Override
    protected void init() {
        RootPanel.get("admin-menu").clear();
        MenuBar validationMenu = new MenuBar(true);
        validationMenu.setAutoOpen(true);
        validationMenu.setAnimationEnabled(true);
        validationMenu.addItem("Missions", new Command() {
            @Override
            public void execute() {
                RootPanel.get("chat-frame").setStyleName("superhidden", true);
                clearScreen();
                Constraint constraint = new Constraint();
                constraint.setAdult(UserTools.isAdult());
                constraint.setValidated(false);
                new Missions(constraint);
            }
        });
        validationMenu.addItem("Pictures", new Command() {
            @Override
            public void execute() {
                RootPanel.get("chat-frame").setStyleName("superhidden", true);
                clearScreen();
                new Picture(0, false);
            }
        });

        // Make a menu bar, adding a few cascading menus to it.
        MenuBar menu = new MenuBar();
        menu.setAutoOpen(true);
        menu.setAnimationEnabled(true);
        menu.addItem("Dashboard", new Command() {
            @Override
            public void execute() {
                clearScreen();

            }
        });
        menu.addItem("Ban", new Command() {
            @Override
            public void execute() {
                new BanBox();
            }
        });
        menu.addItem("Validation", validationMenu);

        RootPanel.get("admin-menu").add(menu);
    }

    private void clearScreen() {
        appArea.clear();
    }
}
