package org.saltaonelove;

import org.saltaonelove.dto.AuthRequest;
import org.saltaonelove.dto.TraineeDTO;
import org.saltaonelove.dto.TrainerDTO;
import org.saltaonelove.facade.GymFacade;
import org.saltaonelove.model.Training;
import org.saltaonelove.model.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CLIHandler {
    private static final Logger log = LoggerFactory.getLogger(CLIHandler.class);
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
        menuActions.put(4, () -> withAuth(gymFacade::showTrainees));
        menuActions.put(5,  () -> withAuth(gymFacade::showTrainers));
        menuActions.put(6, gymFacade::showTrainings);
        menuActions.put(7, () -> withAuth(this::updateTrainee));
        menuActions.put(8, () -> withAuth(this::updateTrainer));
        menuActions.put(9, () -> withAuth(this::deleteTrainee));
        menuActions.put(10, () -> withAuth(gymFacade::showTraineeProfile));
        menuActions.put(11, () -> withAuth(gymFacade::showTrainerProfile));
        menuActions.put(12, () -> withAuth(gymFacade::toggleActivationForTrainee));
        menuActions.put(13, () -> withAuth(gymFacade::toggleActivationForTrainer));
        menuActions.put(14, () -> withAuth(this::showTraineeTrainingByCriteria));
        menuActions.put(15, () -> withAuth(this::showTrainerTrainingByCriteria));
        menuActions.put(16, () -> withAuth(this::changePasswordForTrainee));
        menuActions.put(17, () -> withAuth(this::changePasswordForTrainer));
        menuActions.put(18, () -> System.out.println("Exiting program..."));
    }

    private void withAuth(Consumer<AuthRequest> action) {
        AuthRequest auth = authorize();
        action.accept(auth);
    }

    public void run() {
        while (true) {
            try{
                showMenu();
                int choice = getIntegerMenuChoice();
                if (choice == 18) break;

                menuActions.getOrDefault(choice,
                        () -> System.out.println("Invalid choice. Please try again.")).run();
            } catch (RuntimeException e){
                log.error(e.getMessage());
            }
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
            case 10 -> "Show Trainee Profile";
            case 11 -> "Show Trainer Profile";
            case 12 -> "Toggle active Trainee";
            case 13 -> "Toggle active Trainer";
            case 14 -> "Show Trainings of Trainee with criteria";
            case 15 -> "Show Trainings of Trainer with criteria";
            case 16 -> "Change Password for Trainee";
            case 17 -> "Change Password for Trainer";
            case 18 -> "Exit";
            default -> "Unknown Option";
        };
    }

    private int getIntegerMenuChoice() {
        System.out.print("Enter your choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
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

    private String getStringInputWithNullableOption(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? null : input;
    }

    private long getLongInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextLong()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next();
        }
        long value = scanner.nextLong();
        scanner.nextLine();
        return value;
    }

    public String getValidInput(String prompt, List<String> options) {
        while (true) {
            String userInput = getStringInput(prompt);
            for (String option : options) {
                if (option.equalsIgnoreCase(userInput)) {
                    return userInput;
                }
            }
            System.out.println("Invalid input! Please enter one of: " + String.join(", ", options));
        }
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
        String specialization = getValidInput("Enter specialization (training type): ",
                gymFacade.getTrainingTypes().stream().map(TrainingType::getName).collect(Collectors.toList())
        );
        gymFacade.registerTrainer(firstName, lastName, specialization);
        System.out.println("✅ Trainer registered successfully!");
    }

    private void registerTraining() {
        long trainerId = getLongInput("Enter Trainer ID: ");
        long traineeId = getLongInput("Enter Trainee ID: ");
        String trainingName = getStringInput("Enter training name: ");
        long category = getLongInput("Enter training type id: ");

        gymFacade.registerTraining(trainerId, traineeId, LocalDate.now(), 50L, trainingName, category);
        System.out.println("✅ Training registered successfully!");
    }

    private void updateTrainer(AuthRequest auth) {
        TrainerDTO updatedTrainer = new TrainerDTO(
                getStringInput("Enter new first name: "),
                getStringInput("Enter new last name: "),
                getValidInput("Enter new specialization: ",
                        gymFacade.getTrainingTypes().stream().map(TrainingType::getName).collect(Collectors.toList())
                )
        );
        gymFacade.updateTrainer(auth, updatedTrainer);
        System.out.println("✅ Trainer updated successfully!");
    }

    private void updateTrainee(AuthRequest auth) {
        TraineeDTO updatedTrainee = new TraineeDTO(
                getStringInput("Enter new first name: "),
                getStringInput("Enter new last name: "),
                getStringInput("Enter new birthday (yyyy-MM-dd): "),
                getStringInput("Enter new address: ")
        );
        gymFacade.updateTrainee(auth, updatedTrainee);
        System.out.println("✅ Trainee updated successfully!");
    }

    private void changePasswordForTrainee(AuthRequest auth) {
        gymFacade.changeTraineePassword(auth, getStringInput("Enter new password: "));
    }

    private void changePasswordForTrainer(AuthRequest auth) {
        gymFacade.changeTrainerPassword(auth, getStringInput("Enter new password: "));
    }

    private void deleteTrainee(AuthRequest auth) {
        gymFacade.deleteTrainee(getStringInput("Enter Trainee username: "));
        System.out.println("✅ Delete Trainee successfully!");
    }

    public void showTraineeTrainingByCriteria(AuthRequest auth) {
        String from = getStringInputWithNullableOption("Enter from date(yyyy-MM-dd) (Criteria, nullable): ");
        String to = getStringInputWithNullableOption("Enter to date(yyyy-MM-dd) (Criteria, nullable): ");
        String trainerUsername = getStringInputWithNullableOption("Enter trainer username (Criteria, nullable): ");
        String trainingType = getStringInputWithNullableOption("Enter training type: ");
        gymFacade.getTraineeTrainingByCriteria(auth,
                from != null ? LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null,
                to != null ? LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd")): null,
                trainerUsername, trainingType);
    }

    public void showTrainerTrainingByCriteria(AuthRequest auth) {
        String from = getStringInputWithNullableOption("Enter from date(yyyy-MM-dd) (Criteria, nullable): ");
        String to = getStringInputWithNullableOption("Enter to date(yyyy-MM-dd) (Criteria, nullable): ");
        String traineeUsername = getStringInputWithNullableOption("Enter trainee username: ");
        String trainingType = getStringInputWithNullableOption("Enter training type: ");
        gymFacade.getTrainerTrainingByCriteria(auth,
                from != null ? LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd")): null,
                to != null ? LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null,
                traineeUsername, trainingType);
    }

    private AuthRequest authorize(){
        System.out.println("You need to login first!");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");
        return new AuthRequest(username, password);
    }

}