package org.vaadin.example;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.data.Person;
import org.vaadin.example.data.PersonService;

@Route(value = "person", layout = MainLayout.class)
@Menu(title = "People", icon = "vaadin:users")
public class PersonView extends VerticalLayout {

    public PersonView(@Autowired PersonService personService) {

        Grid<Person> grid = new Grid<>(Person.class);
        grid.setItems(personService.findAll());
        grid.setColumns("firstName", "lastName", "age", "email");
        add(grid);

    }
}
