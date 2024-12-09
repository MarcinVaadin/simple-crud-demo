package org.vaadin.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
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

    private Person currentPerson;

    private TextField firstName;
    private TextField lastName;
    private IntegerField age;
    private EmailField email;

    private Button save;

    public PersonView(@Autowired PersonService personService, @Autowired PersonDataProvider dataProvider) {

        ConfigurableFilterDataProvider filteredDataProvider = dataProvider.withConfigurableFilter();

        Grid<Person> grid = new Grid<>(Person.class);
        grid.setDataProvider(filteredDataProvider);
        grid.setColumns("firstName", "lastName", "age", "email");

        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        age = new IntegerField("Age");
        email = new EmailField("Email");

        Binder<Person> binder = new BeanValidationBinder<>(Person.class);
        binder.setBean(currentPerson);
        binder.bind(firstName, "firstName");
        binder.bind(lastName, "lastName");
        binder.bind(age, "age");
        binder.forField(email)
                .withValidator(
                        email -> email.endsWith("@gmail.com"),
                        "Only gmail.com email addresses are allowed")
                        .bind(Person::getEmail, Person::setEmail);

        grid.addSelectionListener(event -> {
            // in real life list would be backed by list dto with limited fields
            // we might want to fetch entity details by id
            if (event.getFirstSelectedItem().isPresent()) {
                Long id = event.getFirstSelectedItem().get().getId();
                this.currentPerson = personService.find(id);
            } else {
                this.currentPerson = null;
            }
            binder.setBean(currentPerson);
        });

        save = new Button("Save");
        save.addClickListener(e -> {
            try {
                if (this.currentPerson == null) {
                    this.currentPerson = new Person();
                }
                binder.writeBean(currentPerson);
                personService.save(currentPerson);
                dataProvider.refreshItem(currentPerson);
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }
        });

        VerticalLayout masterLayout = new VerticalLayout();
        masterLayout.add(buildSearchField(filteredDataProvider));
        masterLayout.add(grid);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.add(masterLayout);
        horizontalLayout.add(createDetails());

        add(horizontalLayout);
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

    private Component createDetails() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMaxWidth("30%");
        layout.add(new H3("Person Details"));
        layout.add(firstName);
        layout.add(lastName);
        layout.add(age);
        layout.add(email);
        layout.add(save);
        return layout;
    }
}
