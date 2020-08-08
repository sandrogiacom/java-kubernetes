package com.giacom.java.kubernetes.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.giacom.java.kubernetes.domain.User;
import com.giacom.java.kubernetes.persistence.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id:" + id));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> findAll() {
        List<User> people = new ArrayList<>();
        Iterator<User> iterator = userRepository.findAll().iterator();
        iterator.forEachRemaining(people::add);
        return people;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(User user) {
        userRepository.delete(user);
    }

}
