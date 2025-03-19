package org.saltaonelove;

import org.saltaonelove.model.Trainee;
import org.saltaonelove.model.Trainer;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;

import java.time.LocalDate;

public class InitModels {

    public static Trainee initTrainee(){
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setUsername("John.Doe");
        trainee.setPassword("password123");
        trainee.setDateOfBirth("2001-01-01");
        trainee.setAddress("address1");
        trainee.setActive(true);
        return trainee;
    }

    public static TrainingType initTrainingType(){
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeId(1L);
        trainingType.setName("Cardio");
        return trainingType;
    }

    public static Trainer initTrainer(){
        Trainer trainer = new Trainer();
        trainer.setUserId(2L);
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");
        trainer.setUsername("Jane.Doe");
        trainer.setPassword("password123");
        trainer.setSpecialization(initTrainingType());
        trainer.setActive(true);
        return trainer;
    }

    public static Training initTraining(Trainee trainee, Trainer trainer, TrainingType trainingType){
        Training training = new Training();
        training.setTrainingId(1L);
        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingType.getName() + " Training");
        training.setDate(LocalDate.of(2012,12,12));
        training.setDuration(60L);
        return training;
    }

    public static Training initTraining(){
        Trainee trainee = initTrainee();
        Trainer trainer = initTrainer();
        TrainingType trainingType = initTrainingType();

        Training training = new Training();
        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingType.getName() + " with " + trainer.getUsername());
        training.setDuration(50L);
        return training;
    }
}
