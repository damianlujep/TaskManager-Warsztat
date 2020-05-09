package pl.coderslab;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {

    public static void main(String[] args) {

        selectAnOption();

        try(Scanner scanner = new Scanner(System.in)) {

            while (scanner.hasNextLine()) {
                String userInput = scanner.nextLine();

                    switch (userInput) {
                        case "add":
                            System.out.println("Calling method addTask()");
                            break;
                        case "remove":
                            System.out.println("Calling method removeTask()");
                            break;
                        case "list":
                            System.out.println("List of Tasks:");
                            printTaskList();
                            break;
                        case "exit":
                            System.out.println("Calling method exit()");
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

    public static void printTaskList (){
        String[][] tasks = tasks();

        int counter = 0;
        for (int row = 0; row < tasks.length; row++){
            System.out.print(counter + " : ");
            for (int column = 0; column < tasks[row].length; column++){
                System.out.print(tasks[row][column] + " ");
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

        String[][] taskArray = new String[0][0];
        try(Scanner scanner = new Scanner(newFile)){
            String [] temporal = new String[0];

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");

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
