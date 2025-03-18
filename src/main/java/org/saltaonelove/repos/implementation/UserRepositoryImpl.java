package org.saltaonelove.repos.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.saltaonelove.model.User;
import org.saltaonelove.repos.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public List<String> findUsernamesByBase(String baseUsername) {
        return entityManager.createNamedQuery("findUsernamesByBase", String.class)
                .setParameter("baseUsername", baseUsername).getResultList();
    }

}
