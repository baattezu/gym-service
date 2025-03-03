package org.saltaonelove.dao;

import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.User;
import org.saltaonelove.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    private static final String NAMESPACE = "user";


    private Storage storage;
    private IdGenerator idGenerator;
    private UserUtil userUtil;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setUserUtil(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    public User save(User user) {
        if (user.getUserId() == null) {
            user.setUserId(idGenerator.nextId(NAMESPACE));
            user.setUsername(userUtil.generateUsername(user, storage.findAll(NAMESPACE)));
            user.setPassword(userUtil.generateRandomPassword());
        }
        storage.save(NAMESPACE, user.getUserId(), user);
        return user;
    }

    public User update(User user) {
        storage.save(NAMESPACE, user.getUserId(), user);
        return user;
    }

    public User findById(Long id) {
        return storage.findById(NAMESPACE, id);
    }

    public void delete(Long id) {
        storage.delete(NAMESPACE, id);
    }

    public List<User> findAll() {
        return storage.findAll(NAMESPACE);
    }

}