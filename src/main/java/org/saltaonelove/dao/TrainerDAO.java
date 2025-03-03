package org.saltaonelove.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.annotation.PostConstruct;
import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.util.JsonLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TrainerDAO {
    private static final String NAMESPACE = "trainer";
    private static final Logger log = LoggerFactory.getLogger(TrainerDAO.class);

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
    public void setJsonLoader(JsonLoader jsonLoader) {this.jsonLoader = jsonLoader;}

    public Trainer save(Trainer trainer) {
        if (trainer.getUserId() == null) {
            trainer.setUserId(idGenerator.nextId("user"));
        }
        if (trainer.getTrainerId() == null) {
            trainer.setTrainerId(idGenerator.nextId(NAMESPACE));
        }
        storage.save(NAMESPACE, trainer.getTrainerId(), trainer);
        log.info("Saved trainer: {}", trainer);
        return trainer;
    }

    public Trainer findById(Long id) {
        log.info("Finding trainer by id: {}", id);
        return storage.findById(NAMESPACE, id);
    }

    public void delete(Long id) {
        storage.delete(NAMESPACE, id);
        log.info("Deleted trainer with id {}", id);
    }

    public List<Trainer> findAll() {
        log.info("Finding all trainers");
        return storage.findAll(NAMESPACE);
    }

    @PostConstruct
    public void loadInitialData() {
        log.info("Initializing trainer data...");
        try {
            long maxUserIdFromInit = 0;
            long maxTraineeIdFromInit = 0;
            List<Trainer> trainees = jsonLoader.loadFromJson("trainers.json", new TypeReference<List<Trainer>>() {});
            for (Trainer trainer : trainees) {
                maxUserIdFromInit = Math.max(maxUserIdFromInit, trainer.getUserId());
                maxTraineeIdFromInit = Math.max(maxTraineeIdFromInit, trainer.getTrainerId());
                save(trainer);
            }
            idGenerator.initialize("user", maxUserIdFromInit);
            idGenerator.initialize(NAMESPACE, maxTraineeIdFromInit);
            log.info("Successfully loaded {} trainers into storage.", trainees.size());
        } catch (Exception e) {
            log.error("Error initializing trainer data", e);
        }
    }
}