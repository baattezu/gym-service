package org.saltaonelove.repos;

import org.saltaonelove.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository {
    List<User> findAll();
    List<String> findUsernamesByBase(String username);
    Optional<User> findByUsername(String username);
    User save(User user);

}
