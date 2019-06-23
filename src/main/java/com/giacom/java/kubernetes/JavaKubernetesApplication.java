package com.giacom.java.kubernetes;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.giacom.java.kubernetes.domain.Person;
import com.giacom.java.kubernetes.persistence.PersonRepository;
import com.giacom.java.kubernetes.service.PersonService;

@SpringBootApplication
public class JavaKubernetesApplication {

    @Autowired
    private PersonService service;

    @Autowired
    private PersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(JavaKubernetesApplication.class, args);
    }

    @PostConstruct
    public void checkIfWorks() {

        repository.deleteAll();

        service.create(new Person("Minikube",
                LocalDate.of(2006, 10, 01)));
        service.create(new Person("Kubectl",
                LocalDate.of(1999, 05, 15)));

        List<Person> findAll = service.findAll();
        for (Person person : findAll) {
            System.out.println(person.getId() + ":" + person.getName());
        }

    }

}
