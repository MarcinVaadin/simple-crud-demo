package org.vaadin.example;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.data.Person;
import org.vaadin.example.data.PersonDataProvider;
import org.vaadin.example.data.PersonService;

@Route(value = "person", layout = MainLayout.class)
@Menu(title = "People", icon = "vaadin:users")
public class PersonView extends VerticalLayout {

    public PersonView(@Autowired PersonService personService, @Autowired PersonDataProvider dataProvider) {

        ConfigurableFilterDataProvider filteredDataProvider = dataProvider.withConfigurableFilter();

        Grid<Person> grid = new Grid<>(Person.class);
        grid.setDataProvider(filteredDataProvider);
        grid.setColumns("firstName", "lastName", "age", "email");
        add(buildSearchField(filteredDataProvider));
        add(grid);

    }

    private TextField buildSearchField(ConfigurableFilterDataProvider dataProvider) {
        TextField searchField = new TextField();
        searchField.setWidth("25%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> {
            dataProvider.setFilter(e.getValue());
        });
        return searchField;
    }
}
