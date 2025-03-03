package org.saltaonelove;


import org.saltaonelove.config.AppConfig;
import org.saltaonelove.facade.GymFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class);
             Scanner scanner = new Scanner(System.in)) {
            GymFacade gymFacade = context.getBean(GymFacade.class);
            CLIHandler cliHandler = new CLIHandler(gymFacade, scanner);
            cliHandler.run();
        }
    }
}