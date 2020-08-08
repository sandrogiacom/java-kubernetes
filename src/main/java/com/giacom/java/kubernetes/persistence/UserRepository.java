package com.giacom.java.kubernetes.persistence;

import org.springframework.data.repository.CrudRepository;

import com.giacom.java.kubernetes.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

}

