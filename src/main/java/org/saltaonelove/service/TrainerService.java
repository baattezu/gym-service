package org.saltaonelove.service;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.saltaonelove.dao.TrainerDAO;
import org.saltaonelove.dao.UserDAO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.User;
import org.saltaonelove.util.JsonLoader;
import org.saltaonelove.util.UpdateUtil;
import org.saltaonelove.util.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JsonLoader jsonLoader;

    @PostConstruct
    public void loadInitialData() {
        log.info("Initializing trainer data...");
        try {
            List<TrainerDTO> trainers = jsonLoader.loadFromJson("trainers.json", new TypeReference<List<TrainerDTO>>() {});
            for (TrainerDTO trainerDto : trainers) {
                User user = new User(trainerDto.firstName(), trainerDto.lastName());
                user = userDAO.save(user);

                Trainer trainer = UserMapper.userToTrainer(user);
                trainer.setSpecialization(trainerDto.specialization());
                trainerDAO.save(trainer);
            }
            log.info("Successfully loaded {} trainers into storage.", trainers.size());
        } catch (Exception e) {
            log.error("Error initializing trainer data", e);
        }
    }

    public Trainer registerTrainer(String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user = userDAO.save(user);
        Trainer trainer = UserMapper.userToTrainer(user);
        return trainerDAO.save(trainer);
    }

    public Trainer registerTrainer(String firstName, String lastName, String specialization) {
        User user = new User(firstName, lastName);
        user = userDAO.save(user);
        Trainer trainer = UserMapper.userToTrainer(user);
        trainer.setSpecialization(specialization);
        return trainerDAO.save(trainer);
    }

    public Trainer updateTrainer(Long trainerId, TrainerDTO trainerDto) {
        Trainer trainer = trainerDAO.findById(trainerId);

        UpdateUtil.setIfNotNull(trainerDto.firstName(), trainer::setFirstName);
        UpdateUtil.setIfNotNull(trainerDto.lastName(), trainer::setLastName);
        UpdateUtil.setIfNotNull(trainerDto.specialization(), trainer::setSpecialization);

        return trainerDAO.save(trainer);
    }

    public List<Trainer> listTrainers() {
        return trainerDAO.findAll();
    }
}
