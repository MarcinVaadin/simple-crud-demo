package org.vaadin.example.data;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByLastNameContaining(String lastName, Pageable pageable);

    long countByLastNameContaining(String lastName);

}
