package org.saltaonelove.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.saltaonelove.dao.TraineeDAO;
import org.saltaonelove.dao.UserDAO;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.util.JsonLoader;
import org.saltaonelove.util.UpdateUtil;
import org.saltaonelove.util.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JsonLoader jsonLoader;

    @PostConstruct
    public void loadInitialData() {
        log.info("Initializing trainer data...");
        try {
            List<TraineeDTO> trainees = jsonLoader.loadFromJson("trainees.json", new TypeReference<List<TraineeDTO>>() {});
            for (TraineeDTO traineeDto : trainees) {
                User user = new User(traineeDto.firstName(), traineeDto.lastName());
                user = userDAO.save(user);

                Trainee trainee = UserMapper.userToTrainee(user);
                trainee.setAddress(traineeDto.address());
                trainee.setDateOfBirth(traineeDto.dateOfBirth());
                traineeDAO.save(trainee);
            }
            log.info("Successfully loaded {} trainees into storage.", trainees.size());
        } catch (Exception e) {
            log.error("Error initializing trainee data", e);
        }
    }

    public Trainee registerTrainee(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user = userDAO.save(user);
        Trainee trainee = UserMapper.userToTrainee(user);
        return traineeDAO.save(trainee);
    }

    public Trainee registerTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        User user = new User(firstName, lastName);
        user = userDAO.save(user);
        Trainee trainee = UserMapper.userToTrainee(user);
        trainee.setDateOfBirth(dateOfBirth);
        trainee.setAddress(address);
        return traineeDAO.save(trainee);
    }

    public List<Trainee> listTrainees() {
        return traineeDAO.findAll();
    }

    public Trainee getTraineeById(Long id) {
        return traineeDAO.findById(id);
    }

    public Trainee updateTrainee(Long trainerId, TraineeDTO traineeDto) {
        Trainee trainee = traineeDAO.findById(trainerId);

        UpdateUtil.setIfNotNull(traineeDto.firstName(), trainee::setFirstName);
        UpdateUtil.setIfNotNull(traineeDto.lastName(), trainee::setLastName);
        UpdateUtil.setIfNotNull(traineeDto.address(), trainee::setAddress);
        UpdateUtil.setIfNotNull(traineeDto.dateOfBirth(), trainee::setDateOfBirth);

        log.info("Updating trainee: " + trainee.getUsername());
        return traineeDAO.save(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeDAO.delete(id);
    }


}

