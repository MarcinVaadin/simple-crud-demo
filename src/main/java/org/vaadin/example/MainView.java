package org.vaadin.example;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextArea;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(layout = MainLayout.class)
@Menu(title = "Hello", order = 1, icon = "vaadin:comment-o")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed bean.
     */
    public MainView(@Autowired GreetService service) {
        TextField firstNameField = new TextField("Imię");
        firstNameField.setMinLength(3);
        firstNameField.setMaxLength(32);
        firstNameField.setHelperText("Wprowadź swoje imię");
        firstNameField.setPlaceholder("Imię");

        TextField lastNameField = new TextField("Nazwisko");
        lastNameField.setMinLength(3);
        lastNameField.setMaxLength(32);
        lastNameField.setHelperText("Wprowadź swoje nazwisko");
        lastNameField.setPlaceholder("Nazwisko");

        TextField ageField = new TextField("Wiek");
        ageField.setPattern("[1-9][6-9]|[1-9][0-9]");
        ageField.setHelperText("Wprowadź swój wiek");
        ageField.setPlaceholder("Wiek");

        ComboBox<String> countryField = new ComboBox<>("Kraj");
        countryField.setItems("USA", "Kanada", "Meksyk");
        countryField.setHelperText("Wybierz swój kraj");
        countryField.setPlaceholder("Kraj");

        TextField streetField = new TextField("Ulica");
        streetField.setHelperText("Wprowadź swoją ulicę");
        streetField.setPlaceholder("Ulica");

        TextField houseNumberField = new TextField("Numer domu");
        houseNumberField.setHelperText("Wprowadź numer domu");
        houseNumberField.setPlaceholder("Numer domu");

        TextArea orderCommentField = new TextArea("Komentarz do zamówienia");
        orderCommentField.setHelperText("Dodaj komentarz do zamówienia");
        orderCommentField.setPlaceholder("Komentarz");

        add(new HorizontalLayout(firstNameField, lastNameField),
            new HorizontalLayout(ageField, countryField),
            new HorizontalLayout(streetField, houseNumberField),
            orderCommentField);

        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addClassName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello", e -> {
            add(new Paragraph(service.greet(textField.getValue())));
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in
        // styles.css.
        addClassName("centered-content");

        add(textField, button);
    }
}
