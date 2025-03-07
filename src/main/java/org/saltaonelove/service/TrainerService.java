package org.saltaonelove.service;

import org.saltaonelove.dao.TrainerDAO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.util.UpdateUtil;
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
    private UserCredentialsService userUtil;


    public Trainer registerTrainer(TrainerDTO trainerDTO) {
        Trainer trainer = new Trainer(trainerDTO.firstName(), trainerDTO.lastName(), trainerDTO.specialization());
        trainer.setUsername(userUtil.generateUsername(trainer));
        trainer.setPassword(userUtil.generateRandomPassword());
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
