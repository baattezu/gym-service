package org.saltaonelove.util;

import org.junit.jupiter.api.Test;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void testUserToTrainee() {
        User user = new User("Ace", "Ventura");
        Trainee trainee = UserMapper.userToTrainee(user);
        assertNotNull(trainee, "Trainee should not be null");
        assertEquals(user.getFirstName(), trainee.getFirstName(), "Firstname should be the same");
        assertEquals(user.getLastName(), trainee.getLastName(), "Lastname should be the same");
    }
}