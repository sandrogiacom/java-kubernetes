package com.giacom.java.kubernetes.persistence;

import org.springframework.data.repository.CrudRepository;

import com.giacom.java.kubernetes.domain.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

}

