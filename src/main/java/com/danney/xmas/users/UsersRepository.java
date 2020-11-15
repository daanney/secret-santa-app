package com.danney.xmas.users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Integer> {

    Optional<User> findByName(String name);
    Optional<User> findByToken(String token);
}
