package org.saltaonelove.util;

import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;

public class UserMapper {

    public static Trainee userToTrainee(User user) {
        Trainee trainee = new Trainee(user.getFirstName(), user.getLastName());
        trainee.setUserId(user.getUserId());
        trainee.setUsername(user.getUsername());
        trainee.setPassword(user.getPassword());
        trainee.setActive(user.isActive());
        return trainee;
    }

    public static Trainer userToTrainer(User user) {
        Trainer trainer = new Trainer(user.getFirstName(), user.getLastName());
        trainer.setUserId(user.getUserId());
        trainer.setUsername(user.getUsername());
        trainer.setPassword(user.getPassword());
        trainer.setActive(user.isActive());
        return trainer;
    }
}
