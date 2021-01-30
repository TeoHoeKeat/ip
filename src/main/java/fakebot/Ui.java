package fakebot;

import fakebot.task.TaskList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Ui Class use for reading and printing data.
 */
public class Ui {

    private static String DIVIDER = "____________________________________________________________\n";

    private Scanner scanf;

    /**
     * Class constructor for Ui.
     */
    public Ui() {
        scanf = new Scanner(System.in);
    }


    /**
     * Print Standard Message.
     *
     * @param message Message to print.
     */
    public void printBotMessage(String message) {
        String printMessage = DIVIDER + message + "\n" + DIVIDER;
        print(printMessage);
    }

    /**
     * Print List of String.
     *
     * @param startingMessage Starting message to print before the list.
     * @param messages        List of String to print.
     */
    public void printList(String startingMessage, List<String> messages) {
        StringBuilder printMessage = new StringBuilder(DIVIDER);
        printMessage.append(startingMessage);
        for (int i = 1; i <= messages.size(); i++) {
            printMessage.append(i);
            printMessage.append(".");
            printMessage.append(messages.get(i - 1));
            printMessage.append("\n");
        }
        printMessage.append(DIVIDER);
        print(printMessage.toString());
    }

    /**
     * Print List of Task.
     *
     * @param taskList List of Task to print.
     */
    public void printTasks(TaskList taskList) {
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < taskList.getSize(); i++) {
            messages.add(taskList.getTask(i).toString());
        }
        printList("Here are the tasks in your list:\n", messages);
    }

    /**
     * Print Message.
     *
     * @param message message to print.
     */
    private void print(String message) {
        System.out.println(message);
    }

    /**
     * Read Line from IO.
     *
     * @return Return String read from input.
     */
    public String readLine() {
        String input = scanf.nextLine();
        return input;
    }
}
