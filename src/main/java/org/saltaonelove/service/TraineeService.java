package org.saltaonelove.service;

import org.saltaonelove.dao.TraineeDAO;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.util.UpdateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TraineeService {

    private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeDAO traineeDAO;
    @Autowired
    private UserCredentialsService userUtil;

    public Trainee registerTrainee(TraineeDTO traineeDTO) {
        Trainee trainee = new Trainee(
                traineeDTO.firstName(), traineeDTO.lastName(),
                traineeDTO.dateOfBirth() != null ? LocalDate.parse(traineeDTO.dateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null,
                traineeDTO.address()
        );
        trainee.setUsername(userUtil.generateUsername(trainee));
        trainee.setPassword(userUtil.generateRandomPassword());
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

