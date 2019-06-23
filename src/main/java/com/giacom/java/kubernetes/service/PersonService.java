package com.giacom.java.kubernetes.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.giacom.java.kubernetes.domain.Person;
import com.giacom.java.kubernetes.persistence.PersonRepository;

@Service
public class PersonService {

    private PersonRepository personPersistence;

    public PersonService(PersonRepository personPersistence) {
        this.personPersistence = personPersistence;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Person create(Person person) {
        return personPersistence.save(person);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Person findById(Long id) {
        return personPersistence.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Person not found with id:" + id));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Person> findAll() {
        List<Person> people = new ArrayList<>();
        Iterator<Person> iterator = personPersistence.findAll().iterator();
        iterator.forEachRemaining(people::add);
        return people;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Person person) {
        personPersistence.delete(person);
    }

}
