package org.saltaonelove.dao;

import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TrainingDAO {

    private static final String NAMESPACE = "training";

    private Storage storage;
    private IdGenerator idGenerator;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Autowired
    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public Training get(Long id) {
        return storage.findById(NAMESPACE, id);
    }

    public Training save(Training training) {
        if (training.getTrainingId() == null) {
            training.setTrainingId(idGenerator.nextId(NAMESPACE));
        }
        storage.save(NAMESPACE, training.getTrainingId(), training);
        return training;
    }

    public void delete(Long id) {
        storage.delete(NAMESPACE, id);
    }

    public List<Training> findAll() {
        return storage.findAll(NAMESPACE);
    }
}