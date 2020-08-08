package com.giacom.java.kubernetes;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.giacom.java.kubernetes.domain.User;
import com.giacom.java.kubernetes.persistence.UserRepository;
import com.giacom.java.kubernetes.service.UserService;

@SpringBootApplication
public class JavaKubernetesApplication {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(JavaKubernetesApplication.class, args);
    }

    @PostConstruct
    public void checkIfWorks() {

        repository.deleteAll();

        service.create(new User("Minikube",
                LocalDate.of(2006, 10, 01)));
        service.create(new User("Kubectl",
                LocalDate.of(1999, 05, 15)));

        List<User> findAll = service.findAll();
        for (User user : findAll) {
            System.out.println(user.getId() + ":" + user.getName());
        }

    }

}
