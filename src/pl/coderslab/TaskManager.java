package pl.coderslab;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {

    public static String[][] taskArray;

    public static void main(String[] args) {

        selectAnOption();
        tasks();

        try(Scanner scanner = new Scanner(System.in)) {

            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();

                    switch (userInput) {
                        case "add":
                            addTasks();
                            break;
                        case "remove":
                            if (taskArray.length == 0){
                                System.out.println(ConsoleColors.YELLOW + "There is no Tasks available. Please add a Task to continue.");
                            } else {
                                removeTasks();
                            }
                            break;
                        case "list":
                            System.out.println(ConsoleColors.CYAN_BOLD + "List of Tasks:" +ConsoleColors.WHITE);
                            if (taskArray.length == 0){
                                System.out.println(ConsoleColors.YELLOW + "There is no Tasks available. Please add a Task to continue.");
                            } else {
                                printTaskList();
                            }
                            break;
                        case "exit":
                            addTaskToFile(taskArray);
                            System.out.println(ConsoleColors.YELLOW + "Your changes have been saved");
                            System.out.println(ConsoleColors.RED + "Bye, bye.");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Please select a correct option");
                    }
                    System.out.println();
                    selectAnOption();
            }

        }catch (InputMismatchException e){
            System.out.println("Please select a correct option");
        }
    }

    public static void addTasks() {
        System.out.println("Please add task description:");

        Scanner scanner = new Scanner(System.in);
        String task = scanner.nextLine();

        System.out.println("Please add task due date in format YYYY-MM-DD");
        String taskDate = scanner.nextLine();


        boolean dataValidation = false;
        while (!dataValidation) {
            //Variable to validate if input contain numbers YYYYMMDD
            String isNumber = taskDate.replaceAll("-", "");

            //Checking if input contains numbers and (-) in the correct spots
            if (NumberUtils.isParsable(isNumber) && taskDate.length() == 10 && taskDate.charAt(4) == '-' && taskDate.charAt(7) == '-') {
                dataValidation = true;
            } else {
                System.out.println("Please enter a correct due date as per format YYYY-MM-DD");
                dataValidation = false;
                taskDate = scanner.nextLine();
            }
        }

        System.out.println("Is your task important? Select true or false:");
        String taskIsImportant = scanner.nextLine().toLowerCase();

        //Validation to accept just values "true" or "false" as input
        while (!taskIsImportant.equals("true") && !taskIsImportant.equals("false")) {
            System.out.println("Please select a true or false");
            taskIsImportant = scanner.nextLine().toLowerCase();
        }

//        Additional Validation to add IMPORTANT in red on console instead of "true"

//        if (taskIsImportant.equals("true")){
//            taskIsImportant = ConsoleColors.RED + "IMPORTANT" +ConsoleColors.WHITE;
//        } else {
//            taskIsImportant = "Not Important";
//        }

        String[] newTask = {task, taskDate, taskIsImportant};

        //Creating temporal [][] array with task from file existing array
        String[][] temporal = taskArray;
        for (int row = 0; row <taskArray.length; row++) {
            for (int column = 0; column < 3; column++) {
                taskArray[row][column] = temporal[row][column];
            }
        }

        //Adding one row to taskArray[]
        taskArray = new String[temporal.length + 1][3];

        //Copy elements from temporal [] to taskArray[]
        for (int row = 0; row <taskArray.length - 1; row++) {
            for (int column = 0; column < 3; column++){
                taskArray[row][column] = temporal [row][column];
            }
        }

        //Adding new task
        for (int column = 0; column < 3; column++){
            taskArray[taskArray.length-1][column] = newTask [column];
        }
    }

    public static void removeTasks(){

        System.out.println(ConsoleColors.CYAN_BOLD + "Please select the number # of the task to remove:" + ConsoleColors.WHITE);
        printTaskList();
        Scanner scanner = new Scanner(System.in);
        String number = scanner.nextLine();

        boolean dataValidation = false;
        while (!dataValidation) {
            if (NumberUtils.isParsable(number)){
                int isANumber = NumberUtils.toInt(number);
                if (isANumber >= 0 && isANumber < taskArray.length){
                    dataValidation = true;
                } else{
                    System.out.println("Incorrect argument passed. Please enter a  number greater or equal 0");
                    number = scanner.nextLine();
                    dataValidation = false;
                }
            } else {
                System.out.println("Incorrect argument passed. Please enter a  number greater or equal 0");
                number = scanner.nextLine();
                dataValidation = false;
            }
        }

        int taskToRemove = NumberUtils.toInt(number);

        String[][] temporal = taskArray;
        for (int row = 0; row <taskArray.length; row++) {
            for (int column = 0; column < 3; column++) {
                taskArray[row][column] = temporal[row][column];
            }
        }

        //Removing one row from taskArray[]
        taskArray = new String[temporal.length - 1][3];

        //Copy elements from temporal [] to new taskArray[], excluding taskToRemove
        for (int row = 0; row < taskToRemove; row++){
            for (int column = 0; column < 3; column++){
                taskArray[row][column] = temporal[row][column];
            }
        }

        for (int row = taskToRemove+1; row < temporal.length; row++) {
            for (int column = 0; column < 3; column++){
                taskArray[row-1][column]= temporal [row][column];
            }
        }

        System.out.println("The task " + ConsoleColors.RED+taskToRemove + ConsoleColors.WHITE+" was successfully deleted");
    }

    private static void addTaskToFile (String newTask [][]){
        try (FileWriter fileWriter = new FileWriter("tasks.csv",false)){
            for (int row = 0; row < newTask.length; row++){
                for (int column = 0; column < 3; column++){
                    if (column == 0 || column == 1) {
                        fileWriter.append(newTask[row][column] + ",").append(" ");
                    } else {
                        fileWriter.append(newTask [row][column]+"\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void printTaskList (){

        //taskArray as the array, which contends all the tasks from file
        int counter = 0;
        for (int row = 0; row < taskArray.length; row++){
            System.out.print(counter + " : ");
            for (int column = 0; column < taskArray[row].length; column++){
                System.out.print(taskArray[row][column] + "  ");
            }
            System.out.println();
            counter++;
        }
    }

    private static String [][] tasks(){

        //Create file if doesn't exists
        File newFile = new File("tasks.csv");
        if (!newFile.exists()){
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Was not possible to create the file tasks.csv");
                e.printStackTrace();
            }
        }

        //Reading file tasks.csv

        try(Scanner scanner = new Scanner(newFile)){
            String [] temporal = new String[0];

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(", ");

                for (int i = 0; i < split.length; i++){
                  temporal = Arrays.copyOf(temporal,temporal.length+1);
                  temporal[temporal.length-1] = split[i];
                }
            }
//            //**TEST** - Checking temporal [] array with file's text
//            System.out.println(Arrays.toString(temporal));
//            System.out.println("*****************************");

            //Creating new array, with tasks.csv values
            taskArray = new String[temporal.length / 3][3];

            //Extracting values from temporal [] array
            int temporalElementCount = 0;
            for (int row = 0; row <taskArray.length; row++) {
                    for (int column = 0; column < taskArray[row].length; column++) {
                        taskArray[row][column] = temporal[temporalElementCount];
                        temporalElementCount++;
                    }
                }

//            System.out.println(Arrays.deepToString(taskArray));

        } catch (FileNotFoundException e) {
            System.out.println("File tasks.csv not found");
            e.printStackTrace();
        }

        return taskArray;
    }

    private static void selectAnOption(){
        System.out.println(ConsoleColors.BLUE +"Please select an option:");
        String[] options = {"add","remove","list","exit"};

        for (int i=0; i<options.length; i++){
            System.out.println(ConsoleColors.WHITE+options[i]);
        }
    }

}
