package org.saltaonelove.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Entity
@Table(name = "trainee")
@PrimaryKeyJoinColumn(name = "trainee_id", referencedColumnName = "user_id")
@NamedQuery(
        name="Trainee.findByUsername",
        query="SELECT te FROM Trainee te WHERE te.username LIKE :username"
)
@Getter
@Setter
public class Trainee extends User {

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column(name = "address")
    private String address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<Trainer> trainers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trainee", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Training> trainings;


    public Trainee() {
    }

    public Trainee(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Trainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format(
                "Trainee { Trainee ID: %s | Username: %s | First Name: %s | Last Name: %s | Date Of Birth: %s | Address: %s }",
                getUserId(), getUsername(), getFirstName(), getLastName(), dateOfBirth, address
        );
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
