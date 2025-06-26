package org.saltaonelove.repos.implementation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.saltaonelove.model.entity.User;
import org.saltaonelove.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public List<String> findUsernamesByBase(String baseUsername) {
        return entityManager.createNamedQuery("User.findUsernamesByBase", String.class)
                .setParameter("baseUsername", baseUsername + "%").getResultList();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return Optional.of(entityManager.createNamedQuery("User.findByUsername", User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            throw new IllegalArgumentException("Could not find trainer with username: " + username);
        } catch (Exception e) {
            log.error("Unexpected error while fetching trainee: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public User save(User user) {
        try {
            if (user.getUserId() == null) {
                entityManager.persist(user);
            } else {
                entityManager.merge(user);
            }
            return user;
        } catch (Exception e){
            log.error("Error while saving user: {}", e.getMessage());
            throw new RuntimeException("Error while saving user" ,e);
        }
    }

    @Override
    public String findUserPositionByUsername(String username) {
        try {
            return entityManager.createNamedQuery("User.findUserPositionByUsername", String.class)
                    .setParameter("username", username).getSingleResult();
        } catch (Exception e ){
            log.error("Error while fetching user position: {}", e.getMessage());
            throw new IllegalArgumentException("Could not find user position with username: " + username);
        }
    }


}
