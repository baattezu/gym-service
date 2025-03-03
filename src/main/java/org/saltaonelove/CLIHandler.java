package org.saltaonelove;

import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.facade.GymFacade;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CLIHandler {
    private final GymFacade gymFacade;
    private final Scanner scanner;
    private final Map<Integer, Runnable> menuActions = new LinkedHashMap<>();

    public CLIHandler(GymFacade gymFacade, Scanner scanner) {
        this.gymFacade = gymFacade;
        this.scanner = scanner;
        initializeMenu();
    }

    private void initializeMenu() {
        menuActions.put(1, this::registerTrainee);
        menuActions.put(2, this::registerTrainer);
        menuActions.put(3, this::registerTraining);
        menuActions.put(4, gymFacade::showTrainees);
        menuActions.put(5, gymFacade::showTrainers);
        menuActions.put(6, gymFacade::showTrainings);
        menuActions.put(7, this::updateTrainee);
        menuActions.put(8, this::updateTrainer);
        menuActions.put(9, this::deleteTrainee);
        menuActions.put(10, () -> System.out.println("Exiting program..."));
    }

    public void run() {
        while (true) {
            showMenu();
            int choice = getIntegerMenuChoice();
            if (choice == 10) break;

            menuActions.getOrDefault(choice,
                    () -> System.out.println("Invalid choice. Please try again.")).run();
        }
    }

    private void showMenu() {
        System.out.println("\n============================");
        System.out.println("       GYM MANAGEMENT       ");
        System.out.println("============================");
        menuActions.forEach((key, value) ->
                System.out.println(key + ". " + getMenuOptionText(key)));
        System.out.println("============================");
    }

    private String getMenuOptionText(int option) {
        return switch (option) {
            case 1 -> "Register Trainee";
            case 2 -> "Register Trainer";
            case 3 -> "Register Training";
            case 4 -> "Show Trainees";
            case 5 -> "Show Trainers";
            case 6 -> "Show Trainings";
            case 7 -> "Update Trainee";
            case 8 -> "Update Trainer";
            case 9 -> "Delete Trainee";
            case 10 -> "Exit";
            default -> "Unknown Option";
        };
    }

    private int getIntegerMenuChoice() {
        System.out.print("Enter your choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clear invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        String input;
        while ((input = scanner.nextLine().trim()).isEmpty()) {
            System.out.println("Input cannot be empty. Please enter a valid value.");
        }
        return input;
    }

    private long getLongInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextLong()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clear invalid input
        }
        long value = scanner.nextLong();
        scanner.nextLine(); // Consume newline
        return value;
    }


    private void registerTrainee() {
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        gymFacade.registerTrainee(firstName, lastName);
        System.out.println("✅ Trainee registered successfully!");
    }

    private void registerTrainer() {
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        gymFacade.registerTrainer(firstName, lastName);
        System.out.println("✅ Trainer registered successfully!");
    }

    private void registerTraining() {
        long trainerId = getLongInput("Enter Trainer ID: ");
        long traineeId = getLongInput("Enter Trainee ID: ");
        String trainingName = getStringInput("Enter training name: ");
        String category = getStringInput("Enter training category: ");

        gymFacade.registerTraining(trainerId, traineeId, LocalDate.now(), Duration.ofMinutes(50), trainingName, category);
        System.out.println("✅ Training registered successfully!");
    }

    private void updateTrainer() {
        long id = getLongInput("Enter Trainer ID: ");
        TrainerDTO updatedTrainer = new TrainerDTO(
                getStringInput("Enter new first name: "),
                getStringInput("Enter new last name: "),
                getStringInput("Enter new specialization: ")
        );
        gymFacade.updateTrainer(id, updatedTrainer);
        System.out.println("✅ Trainer updated successfully!");
    }

    private void updateTrainee() {
        long id = getLongInput("Enter Trainee ID: ");
        TraineeDTO updatedTrainee = new TraineeDTO(
                getStringInput("Enter new first name: "),
                getStringInput("Enter new last name: "),
                getStringInput("Enter new birthday (yyyy-MM-dd): "),
                getStringInput("Enter new address: ")
        );
        gymFacade.updateTrainee(id, updatedTrainee);
        System.out.println("✅ Trainee updated successfully!");
    }

    private void deleteTrainee() {
        long id = getLongInput("Enter Trainee ID: ");
        gymFacade.deleteTrainee(id);
        System.out.println("✅ Delete Trainee successfully!");
    }

}