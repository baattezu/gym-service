package org.saltaonelove.repos;

import org.saltaonelove.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    List<User> findAll();
}
