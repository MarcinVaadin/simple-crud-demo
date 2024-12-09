package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("My Application");
        title.setClassName(LumoUtility.FontSize.LARGE);

        SideNav nav = getSideNav();

        addToDrawer(nav);
        addToNavbar(toggle, title);
    }

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.stream().map(this::toSideNavItem).forEach(sideNav::addItem);
        return sideNav;
    }

    private SideNavItem toSideNavItem(MenuEntry menuEntry) {
        SideNavItem item = new SideNavItem(menuEntry.title());
        item.setPath(menuEntry.path());
        item.setPrefixComponent(new Icon(menuEntry.icon()));
        return item;
    }
}
