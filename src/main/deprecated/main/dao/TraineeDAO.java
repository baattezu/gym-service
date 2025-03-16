package org.saltaonelove.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Deprecated
@Repository
public class TraineeDAO {

    private static final String NAMESPACE = "trainee";
    private static final Logger log = LoggerFactory.getLogger(TraineeDAO.class);


    private Storage storage;
    private IdGenerator idGenerator;
    private JsonLoader jsonLoader;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Autowired
    public void setJsonLoader(JsonLoader jsonLoader) {
        this.jsonLoader = jsonLoader;
    }

    public Trainee save(Trainee trainee) {
        if (trainee.getUserId() == null) {
            trainee.setUserId(idGenerator.nextId("user"));
        }
        if (trainee.getTraineeId() == null) {
            trainee.setTraineeId(idGenerator.nextId(NAMESPACE));
        }
        storage.save(NAMESPACE, trainee.getTraineeId(), trainee);
        log.info("Saved trainee: {}", trainee);
        return trainee;
    }

    public Trainee findById(Long id) {
        return storage.findById(NAMESPACE, id);
    }

    public void delete(Long id) {
        storage.delete(NAMESPACE, id);
    }

    public List<Trainee> findAll() {
        return storage.findAll(NAMESPACE);
    }

    @PostConstruct
    public void loadInitialData() {
        log.info("Initializing trainee data...");
        try {
            long maxUserIdFromInit = 0;
            long maxTraineeIdFromInit = 0;
            List<Trainee> trainees = jsonLoader.loadFromJson("trainees.json", new TypeReference<List<Trainee>>() {
            });
            for (Trainee trainee : trainees) {
                maxTraineeIdFromInit = Math.max(maxTraineeIdFromInit, trainee.getTraineeId());
                save(trainee);
            }
            idGenerator.initialize("user", maxUserIdFromInit);
            idGenerator.initialize(NAMESPACE, maxTraineeIdFromInit);
            log.info("Successfully loaded {} trainees into storage.", trainees.size());
        } catch (Exception e) {
            log.error("Error initializing trainee data", e);
        }
    }

}