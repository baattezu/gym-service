package org.saltaonelove.dao;

import org.saltaonelove.dao.utils.IdGenerator;
import org.saltaonelove.dao.utils.Storage;
import org.saltaonelove.model.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TrainingTypeDAO {

    private static final String NAMESPACE = "trainingType";

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
    public List<TrainingType> findAll() {
        return storage.findAll(NAMESPACE);
    }

    public TrainingType findById(Long id) {
        return storage.findById(NAMESPACE, id);
    }

    public TrainingType save(TrainingType trainingType) {
        if (trainingType.getId() == null) {
            trainingType.setId(idGenerator.nextId(NAMESPACE));
        }
        storage.save(NAMESPACE, trainingType.getId(), trainingType);
        return trainingType;
    }
}