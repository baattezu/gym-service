package org.saltaonelove.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({
        @NamedQuery(
                name = "User.findUsernamesByBase",
                query = "SELECT user.username FROM User user WHERE user.username LIKE :baseUsername"
        ),
        @NamedQuery(
                name = "User.findByUsername",
                query = "SELECT user FROM User user WHERE user.username LIKE :username"
        ),
        @NamedQuery(
                name = "User.findUserPositionByUsername",
                query =
                """
                SELECT CASE
                    WHEN EXISTS (
                        SELECT 1 FROM Trainer t WHERE t.username = u.username
                    ) THEN 'TRAINER'
                    WHEN EXISTS (
                        SELECT 1 FROM Trainee t WHERE t.username = u.username
                    ) THEN 'TRAINEE'
                    ELSE 'UNKNOWN'
                END
                FROM User u
                WHERE u.username = :username
                """
        )
})
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "first_name")
    @NotNull(message = "First name should not be null")
    private String firstName;
    @Column(name = "last_name")
    @NotNull(message = "Last name should not be null")
    private String lastName;
    @Column(name = "username", updatable = false)
    @NotNull(message = "Username should not be null")
    private String username;
    @Column(name = "password")
    @NotNull(message = "Password should not be null")
    @Size(min = 10, message = "Password should be at least 10 characters")
    private String password;
    @Column(name = "is_active")
    @NotNull(message = "Activation field should not be null")
    private boolean isActive;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName + "." + lastName;
        this.isActive = true;
    }


    @Override
    public String toString() {
        return String.format("User { User ID: %s | Username: %s | First Name: %s | Last Name: %s | IsActive: %s }", userId, username, firstName, lastName, isActive);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isActive == user.isActive && Objects.equals(userId, user.userId) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, username, password, isActive);
    }

    public boolean usernameEquals(Object otherUser) {
        if (otherUser != null && otherUser instanceof User u) {
            return username.equals(u.getUsername().replaceAll("\\d+", ""));
        }
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
