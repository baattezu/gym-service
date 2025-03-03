package org.saltaonelove.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.saltaonelove.dao.TrainingDAO;
import org.saltaonelove.dao.TrainingTypeDAO;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.saltaonelove.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class TrainingService {

    @Autowired
    private TrainingDAO trainingDAO;
    @Autowired
    private TrainingTypeDAO trainingTypeDAO;

    public Training addTrainingType(Training training, String trainingTypeName) {
        TrainingType trainingType = new TrainingType(trainingTypeName);
        if (trainingTypeDAO.findAll().stream().anyMatch(t -> t.getName()
                .equals(trainingType.getName()))) {
            training.setTrainingType(trainingType);
        } else {
            trainingTypeDAO.save(trainingType);
            training.setTrainingType(trainingType);
        }
        return training;
    }

    public Training createTraining(Training training) {
        return trainingDAO.save(training);
    }

    public List<Training> listTrainings() {
        return trainingDAO.findAll();
    }

    public Training getTraining(Long id) {
        return trainingDAO.get(id);
    }

}
