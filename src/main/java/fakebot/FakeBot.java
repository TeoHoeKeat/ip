package fakebot;

import fakebot.command.*;
import fakebot.task.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class FakeBot {
    private static String OLDLOGO = " ____        _        \n"
            + "|  _ \\ _   _| | _____ \n"
            + "| | | | | | | |/ / _ \\\n"
            + "| |_| | |_| |   <  __/\n"
            + "|____/ \\__,_|_|\\_\\___|\n";

    private static String LOGO = " ______      _  ________   ____   ____ _______ \n"
            + "|  ____/ \\  | |/ /  ____| |  _ \\ / __ \\__   __|\n"
            + "| |__ /  \\  | ' /| |__    | |_) | |  | | | |   \n"
            + "|  __/ /\\ \\ |  < |  __|   |  _ <| |  | | | |\n"
            + "| | / ____ \\| . \\| |____  | |_) | |__| | | |\n"
            + "|_|/_/    \\_\\_|\\_\\______| |____/ \\____/  |_|\n";


    private static String EXIT_COMMAND = "bye";
    private static String LIST_COMMAND = "list";
    private static String DONE_COMMAND = "done";
    private static String TODO_COMMAND = "todo";
    private static String DEADLINE_COMMAND = "deadline";
    private static String DEADLINE_SPLIT_REGEX = " /by ";
    private static String EVENT_COMMAND = "event";
    private static String EVENT_SPLIT_REGEX = " /at ";
    private static String DELETE_COMMAND = "delete";

    private static String saveFilePath = "/data/";
    private static String saveFileName = "savedHistory.txt";
    private static Ui ui;
    private static Storage storage;

    public static void main(String[] args) {
        ui = new Ui();
        storage = new Storage(saveFileName, saveFilePath);
        printHelloMessage();
        boolean continueProgram = true;
        TaskList taskList = new TaskList(storage.tryReadTaskFile());
        while (continueProgram) {
            String reply = ui.readLine();
            Command command;
            try {
                command = validateCommand(reply, taskList.getSize());
            } catch (CommandException e) {
                ui.printBotMessage(e.getMessage());
                continue;
            }
            continueProgram = processCommand(taskList, command);
        }
        ui.printBotMessage("Bye. Hope to see you again soon!");
    }

    public static void saveHistory(TaskList task) {
        storage.writeTasksToFIle(task);
    }

    public static void printHelloMessage() {
        ui.printBotMessage("Hello from\n" + LOGO + "What can I do for you?");
    }

    public static void printDoneMessage(Task task) {
        ui.printBotMessage("Nice! I've marked this task as done:\n " + task.toString());
    }

    public static void printDeleteMessage(Task task, int count) {
        ui.printBotMessage("Noted. I've removed this task:\n " + task.toString()
                + "\nNow you have " + count + " tasks in the list.");
    }

    public static void printAddedTaskMessage(Task task, int count) {
        ui.printBotMessage("Got it. I've added this task: \n  " + task.toString()
                + "\nNow you have " + count + " tasks in the list.");
    }

    //Process Command given by user
    public static boolean processCommand(TaskList taskList, Command command) {
        switch (command.getCommand()) {
        case BYE:
            return false;
        case LIST:
            ui.printTasks(taskList);
            break;
        case DONE:
            int doneIndex = Integer.parseInt(command.getDescription()) - 1;
            taskList.getTask(doneIndex).markComplete();
            printDoneMessage(taskList.getTask(doneIndex));
            saveHistory(taskList);
            break;
        case TODO:
            ToDos todoTask = new ToDos(command.getDescription());
            taskList.addTask(todoTask);
            printAddedTaskMessage(todoTask, taskList.getSize());
            saveHistory(taskList);
            break;
        case DEADLINE:
            String[] deadlineDetalis = command.getDescription().split(DEADLINE_SPLIT_REGEX);
            String[] dates = deadlineDetalis[1].split(" ");
            LocalDate date = LocalDate.parse(dates[0]);
            LocalTime time = LocalTime.parse(dates[1]);
            Deadlines deadlineTask = new Deadlines(deadlineDetalis[0], date, time);
            taskList.addTask(deadlineTask);
            printAddedTaskMessage(deadlineTask, taskList.getSize());
            saveHistory(taskList);
            break;
        case EVENT:
            String[] eventDetails = command.getDescription().split(EVENT_SPLIT_REGEX);
            String[] eventDates = eventDetails[1].split(" ");
            LocalDate startDate = LocalDate.parse(eventDates[0]);
            LocalTime startTime = LocalTime.parse(eventDates[1]);
            LocalDate endDate = LocalDate.parse(eventDates[2]);
            LocalTime endTime = LocalTime.parse(eventDates[3]);
            Events eventTask = new Events(eventDetails[0], startDate, startTime, endDate, endTime);
            taskList.addTask(eventTask);
            printAddedTaskMessage(eventTask, taskList.getSize());
            saveHistory(taskList);
            break;
        case DELETE:
            int deleteIndex = Integer.parseInt(command.getDescription()) - 1;
            Task deletedTask = taskList.getTask(deleteIndex);
            taskList.removeTask(deleteIndex);
            printDeleteMessage(deletedTask, taskList.getSize());
            saveHistory(taskList);
            break;
        }

        return true;
    }

    //Validate User Input
    public static Command validateCommand(String command, int taskCount) throws CommandException {
        if (command.equals(EXIT_COMMAND)) {
            return new Command(CommandType.BYE);
        } else if (command.equals(LIST_COMMAND)) {
            return new Command(CommandType.LIST);
        }

        if (command.equals(DONE_COMMAND) || command.equals(DELETE_COMMAND)) {
            throw new CommandException("☹ OOPS!!! You must indicate the index of the Tasks to be " + command + ".");
        }

        if (command.equals(TODO_COMMAND) || command.equals(DEADLINE_COMMAND) || command.equals(EVENT_COMMAND)) {
            throw new CommandException("☹ OOPS!!! The description of a " + command + " cannot be empty.");
        }

        int firstSplit = command.indexOf(' ');
        if (firstSplit < 0) {
            throw new CommandException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
        }

        String commandName = command.substring(0, firstSplit);
        String description = command.substring(firstSplit + 1);

        if (commandName.equals(DONE_COMMAND) || commandName.equals(DELETE_COMMAND) || commandName.equals(TODO_COMMAND)
                || commandName.equals(EVENT_COMMAND) || commandName.equals(DEADLINE_COMMAND)) {
            if (description.isEmpty()) {
                throw new CommandException("☹ OOPS!!! The description of a " + commandName + " cannot be empty.");
            } else if (commandName.equals(DONE_COMMAND) || commandName.equals(DELETE_COMMAND)) {
                try {
                    int index = Integer.parseInt(description);
                    if (index > taskCount || index < 1) {
                        throw new CommandException("☹ OOPS!!! Task number out of range.");
                    }
                } catch (NumberFormatException e) {
                    throw new CommandException("☹ OOPS!!! Invalid Task Index Format.");
                }
                if (commandName.equals(DONE_COMMAND)) {
                    return new Command(CommandType.DONE, description);

                } else if (commandName.equals(DELETE_COMMAND)) {
                    return new Command(CommandType.DELETE, description);
                }
            } else if (commandName.equals(TODO_COMMAND)) {
                return new Command(CommandType.TODO, description);
            } else if (commandName.equals(DEADLINE_COMMAND)) {
                if (!description.contains(DEADLINE_SPLIT_REGEX))
                    throw new CommandException("☹ OOPS!!! The description of a "
                            + DEADLINE_COMMAND + " must contain Date indicated by \""
                            + DEADLINE_SPLIT_REGEX + "\".");

                try {
                    String[] deadlineDetails = description.split(DEADLINE_SPLIT_REGEX);
                    String[] dates = deadlineDetails[1].split(" ");
                    LocalDate date = LocalDate.parse(dates[0]);
                    LocalTime time = LocalTime.parse(dates[1]);
                } catch (Exception e) {
                    throw new CommandException("☹ OOPS!!! The Date format of a "
                            + DEADLINE_COMMAND + " must be yyyy-mm-dd hh:ss.");

                }

                return new Command(CommandType.DEADLINE, description);
            } else if (commandName.equals(EVENT_COMMAND)) {
                if (!description.contains(EVENT_SPLIT_REGEX))
                    throw new CommandException("☹ OOPS!!! The description of a " + EVENT_COMMAND
                            + " must contain Date and Duration indicated by \"" + EVENT_SPLIT_REGEX + "\".");

                try {
                    String[] eventDetails = description.split(EVENT_SPLIT_REGEX);
                    String[] dates = eventDetails[1].split(" ");
                    LocalDate startDate = LocalDate.parse(dates[0]);
                    LocalTime startTime = LocalTime.parse(dates[1]);
                    LocalDate endDate = LocalDate.parse(dates[2]);
                    LocalTime endTime = LocalTime.parse(dates[3]);
                } catch (Exception e) {
                    throw new CommandException("☹ OOPS!!! The Date format of a "
                            + EVENT_COMMAND + " must be yyyy-mm-dd hh:ss yyyy-mm-dd hh:ss.");

                }

                return new Command(CommandType.EVENT, description);
            }
        }

        throw new CommandException(" OOPS!!! I'm sorry, but I don't know what that means :-(");
    }
}
