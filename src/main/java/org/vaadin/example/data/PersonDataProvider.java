package org.vaadin.example.data;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class PersonDataProvider extends AbstractBackEndDataProvider<Person, String> {

    private PersonRepository personRepository;

    public PersonDataProvider(@Autowired PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    protected Stream<Person> fetchFromBackEnd(Query<Person, String> query) {
        if (query.getFilter().isPresent()) {
            return personRepository
                    .findByLastNameContaining(query.getFilter().get(), toPageable(query))
                    .stream();
        }
        return personRepository.findAll(toPageable(query)).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Person, String> query) {
        if (query.getFilter().isPresent()) {
            return (int) personRepository.countByLastNameContaining(query.getFilter().get());
        }
        return (int) personRepository.count();
    }

    private Pageable toPageable(Query<Person, String> query) {
        return PageRequest.of(query.getPage(), query.getPageSize());
    }

}
