package fakebot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import fakebot.command.Command;
import fakebot.command.CommandException;
import fakebot.command.CommandType;
import fakebot.task.Deadline;
import fakebot.task.Event;
import fakebot.task.Task;
import fakebot.task.TaskList;
import fakebot.task.Todo;

/**
 * Parser class use for processing syntax.
 */
public class Parser {

    private static final String DIVIDER = "";
    private static final String SPLIT_REGEX = "-'@,-@,1'-";

    private static final String EXIT_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String DONE_COMMAND = "done";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String DEADLINE_SPLIT_REGEX = " /by ";
    private static final String EVENT_COMMAND = "event";
    private static final String EVENT_SPLIT_REGEX = " /at ";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String HELP_COMMAND = "help";

    private static final String HELP_STRING = "List of Command Available:\n"
            + "Deadline Command: " + DEADLINE_COMMAND + " [Description]" + DEADLINE_SPLIT_REGEX + "\n"
            + "                   [yyyy-mm-dd] [hh:mm]\n"
            + "Delete   Command: " + DELETE_COMMAND + " [index]\n"
            + "Done     Command: " + DONE_COMMAND + " [index]\n"
            + "Event    Command: " + EVENT_COMMAND + " [Description]" + EVENT_SPLIT_REGEX + "[yyyy-mm-dd]\n"
            + "                   [hh:mm]] [yyyy-mm-dd] [hh:mm]\n"
            + "Find     Command: " + FIND_COMMAND + " [Description]\n"
            + "List     Command: " + LIST_COMMAND + "\n"
            + "Todo     Command: " + TODO_COMMAND + " [Description]\n"
            + "Exit     Command: " + EXIT_COMMAND + "\n";


    /**
     * Enum for different task type.
     */
    private enum TaskType {
        TODO, EVENT, DEADLINE
    }

    /**
     * Converts task to string.
     *
     * @param stringList the list of string to be converted to string.
     * @return a string containing string converted from string list.
     */
    public static String convertStringListToString(List<String> stringList) {
        StringBuilder builder = new StringBuilder();
        for (String s : stringList) {
            builder.append(s);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Converts task to string.
     *
     * @param task to be parsed to string.
     * @return a string containing parsed task.
     */
    public static String convertTaskToString(Task task) {
        if (task instanceof Todo) {
            Todo todo = (Todo) task;
            return convertTodoToString(todo);
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return convertEventToString(event);
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return convertDeadlineToString(deadline);
        } else {
            assert false : "Task being handle should be handle";
            return null;
        }
    }

    /**
     * Converts Todo to string.
     *
     * @param todo to be parsed to string.
     * @return a string containing parsed todo.
     */
    private static String convertTodoToString(Todo todo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TaskType.TODO.name());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(todo.isComplete());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(todo.getTaskName());
        return stringBuilder.toString();
    }

    /**
     * Converts Event to string.
     *
     * @param event to be parsed to string.
     * @return a string containing parsed event.
     */
    private static String convertEventToString(Event event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TaskType.EVENT.name());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.isComplete());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.getTaskName());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.getStartDate());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.getStartTime());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.getEndDate());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(event.getEndTime());
        return stringBuilder.toString();
    }

    /**
     * Converts Deadline to string.
     *
     * @param deadline to be parsed to string.
     * @return a string containing parsed Deadline.
     */
    private static String convertDeadlineToString(Deadline deadline) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TaskType.DEADLINE.name());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(deadline.isComplete());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(deadline.getTaskName());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(deadline.getDeadlineDate());
        stringBuilder.append(SPLIT_REGEX);
        stringBuilder.append(deadline.getDeadlineTime());
        return stringBuilder.toString();
    }

    /**
     * Converts list of task to list of string.
     *
     * @param taskList the string of list.
     * @return a list of String parsed from list of task.
     */
    public static List<String> convertTasksToStrings(TaskList taskList) {
        List<String> strings = new ArrayList<String>();

        for (int i = 0; i < taskList.getSize(); i++) {
            Task currentTask = taskList.getTask(i);
            strings.add(convertTaskToString(currentTask));
        }
        return strings;
    }

    /**
     * Converts string to task.
     *
     * @param string the parsed task.
     * @return a task parsed from string.
     */
    public static Task convertStringToTask(String string) {
        String[] parts = string.split(SPLIT_REGEX);
        Task currentTask = null;
        switch (TaskType.valueOf(parts[0])) {
        case TODO:
            currentTask = new Todo(parts[2]);
            break;
        case EVENT:
            currentTask = new Event(parts[2], LocalDate.parse(parts[3]), LocalTime.parse(parts[4]),
                    LocalDate.parse(parts[5]), LocalTime.parse(parts[6]));
            break;
        case DEADLINE:
            currentTask = new Deadline(parts[2], LocalDate.parse(parts[3]), LocalTime.parse(parts[4]));
            break;
        default:
            assert false : "String being parsed should be a type of parsed Task";
            break;
        }

        if (Boolean.parseBoolean(parts[1])) {
            currentTask.markComplete();
        }

        return currentTask;
    }

    /**
     * Converts list of string to list of task.
     *
     * @param stringList the string of list.
     * @return a list of task parsed from list of string.
     */
    public static List<Task> convertStringsToTasks(List<String> stringList) {
        List<Task> tasks = new ArrayList<Task>();

        for (String line : stringList) {
            tasks.add(convertStringToTask(line));
        }
        return tasks;
    }


    /**
     * Returns standard print message.
     *
     * @param message to print.
     * @return a string containing print message.
     */
    public static String getBotMPrintMessage(String message) {
        String printMessage = DIVIDER + message + "\n" + DIVIDER;
        return printMessage;
    }

    /**
     * Returns list of string print message.
     *
     * @param startingMessage to print before the list.
     * @param messages        the list of string to print.
     * @return a string containing print message.
     */
    public static String getStringListPrintMessage(String startingMessage, List<String> messages) {
        StringBuilder printMessage = new StringBuilder(DIVIDER);
        printMessage.append(startingMessage);
        for (int i = 1; i <= messages.size(); i++) {
            printMessage.append(i);
            printMessage.append(".");
            printMessage.append(messages.get(i - 1));
            printMessage.append("\n");
        }
        printMessage.append(DIVIDER);
        return printMessage.toString();
    }

    /**
     * Returns list of task print message.
     *
     * @param taskList the list of task to be convert.
     * @return a string containing print message.
     */
    public static String getTaskListPrintMessage(TaskList taskList) {
        if (taskList.getSize() == 0) {
            return getBotMPrintMessage("Task list is currently empty");
        }

        List<String> messages = new ArrayList<>();
        for (int i = 0; i < taskList.getSize(); i++) {
            messages.add(taskList.getTask(i).toString());
        }
        return getStringListPrintMessage("Here are the tasks in your list:\n", messages);
    }


    /**
     * Parses user input to command.
     *
     * @param command the user input to be parsed.
     * @return a command.
     */
    public static Command parseUserInputToCommand(String command) throws CommandException {
        if (isCommandWithoutDescription(command)) {
            return parseCommandWithoutDescription(command);
        }

        int firstSplit = command.indexOf(' ');
        //Command with description should contain at least one space
        if (firstSplit < 0) {
            if (isCommandWithDescription(command)) {
                throw new CommandException("OOPS!!! The description of a " + command + " cannot be empty.");
            } else if (isCommandWithIndexDescription(command)) {
                throw new CommandException("OOPS!!! You must indicate the index of the Tasks to be " + command + ".");
            }
            throw new CommandException("OOPS!!! I'm sorry, but I don't know what that means :-( , "
                    + "type \"help\" for help.");
        }

        String commandName = command.substring(0, firstSplit);
        String description = command.substring(firstSplit + 1);

        if (isCommandWithIndexDescription(commandName)) {
            return parseCommandWithIndexDescription(commandName, description);
        }
        if (isCommandWithDescription(commandName)) {
            return parseCommandWithDescription(commandName, description);
        }

        throw new CommandException("OOPS!!! I'm sorry, but I don't know what that means :-( , type \"help\" for help.");
    }

    /**
     * Checks if the command need a description.
     *
     * @param command the command to be checked.
     * @return true if the command do not need a description.
     */
    private static Boolean isCommandWithoutDescription(String command) {
        boolean isCommandWithoutDescription = command.equals(EXIT_COMMAND) || command.equals(LIST_COMMAND)
                || command.equals(HELP_COMMAND);
        return isCommandWithoutDescription;
    }

    /**
     * Parses the command that do not need description to Command class.
     *
     * @param command the command that do not need description.
     * @return a command without description.
     */
    private static Command parseCommandWithoutDescription(String command) {
        if (command.equals(EXIT_COMMAND)) {
            return new Command(CommandType.BYE);
        } else if (command.equals(LIST_COMMAND)) {
            return new Command(CommandType.LIST);
        } else if (command.equals(HELP_COMMAND)) {
            return new Command(CommandType.HELP, HELP_STRING);
        } else {
            assert false : "Invalid calling of parse command without description";
            return null;
        }
    }

    /**
     * Check if the command need a index description.
     *
     * @param command the command to be checked.
     * @return true if the command do not need a description.
     */
    private static Boolean isCommandWithIndexDescription(String command) {
        boolean isCommandWithIndexDescription = command.equals(DONE_COMMAND) || command.equals(DELETE_COMMAND);
        return isCommandWithIndexDescription;
    }

    /**
     * Parses the command that need a index description to command class.
     *
     * @param command     the command that need a index description.
     * @param indexString the string containing index.
     * @return a command that need a index description.
     */
    private static Command parseCommandWithIndexDescription(String command, String indexString)
            throws CommandException {
        if (indexString.isEmpty()) {
            throw new CommandException("OOPS!!! You must indicate the index of the Tasks to be " + command + ".");
        }

        try {
            int index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new CommandException("OOPS!!! Invalid Task Index Format.");
        }

        if (command.equals(DONE_COMMAND)) {
            return new Command(CommandType.DONE, indexString);

        } else if (command.equals(DELETE_COMMAND)) {
            return new Command(CommandType.DELETE, indexString);
        } else {
            assert false : "Invalid calling of parse command with index description";
            return null;
        }
    }

    /**
     * Check if the command need a description.
     *
     * @param command the command to be checked.
     * @return true if the command do need a description.
     */
    private static Boolean isCommandWithDescription(String command) {
        boolean isCommandWithDescription = command.equals(TODO_COMMAND) || command.equals(EVENT_COMMAND)
                || command.equals(DEADLINE_COMMAND) || command.equals(FIND_COMMAND);
        return isCommandWithDescription;
    }

    /**
     * Parses the command that need a description to command class.
     *
     * @param command     the name of the command.
     * @param description the description of the command.
     * @return a command that need a description.
     */
    private static Command parseCommandWithDescription(String command, String description) throws CommandException {
        if (description.isEmpty()) {
            throw new CommandException("OOPS!!! The description of a " + command + " cannot be empty.");
        }
        if (command.equals(FIND_COMMAND)) {
            return parseFindCommand(description);
        } else if (command.equals(TODO_COMMAND)) {
            return parseTodoCommand(description);
        } else if (command.equals(DEADLINE_COMMAND)) {
            return parseDeadlineCommand(description);
        } else if (command.equals(EVENT_COMMAND)) {
            return parseEventCommand(description);
        } else {
            assert false : "Invalid calling of parse command with description";
            return null;
        }
    }


    /**
     * Parses todo command.
     *
     * @param description the command description.
     * @return a todo command.
     */
    private static Command parseTodoCommand(String description) {
        return new Command(CommandType.TODO, description);
    }

    /**
     * Parses find command.
     *
     * @param description the command description.
     * @return a find command.
     */
    private static Command parseFindCommand(String description) {
        return new Command(CommandType.FIND, description);
    }

    /**
     * Parses event command.
     *
     * @param description the command description.
     * @return an event command.
     */
    private static Command parseEventCommand(String description) throws CommandException {
        if (!description.contains(EVENT_SPLIT_REGEX)) {
            throw new CommandException("OOPS!!! The description of a " + EVENT_COMMAND
                    + " must contain Date and Duration indicated by \"" + EVENT_SPLIT_REGEX + "\".");
        }
        try {
            String[] eventDetails = description.split(EVENT_SPLIT_REGEX);
            String[] dates = eventDetails[1].split(" ");
            LocalDate startDate = LocalDate.parse(dates[0]);
            LocalTime startTime = LocalTime.parse(dates[1]);
            LocalDate endDate = LocalDate.parse(dates[2]);
            LocalTime endTime = LocalTime.parse(dates[3]);

            return new Command(CommandType.EVENT, eventDetails);
        } catch (Exception e) {
            throw new CommandException("OOPS!!! The Date format of a "
                    + EVENT_COMMAND + " must be yyyy-mm-dd hh:mm yyyy-mm-dd hh:mm");
        }
    }

    /**
     * Parses deadline command.
     *
     * @param description the command description.
     * @return a deadline command.
     */
    private static Command parseDeadlineCommand(String description) throws CommandException {
        if (!description.contains(DEADLINE_SPLIT_REGEX)) {
            throw new CommandException("OOPS!!! The description of a "
                    + DEADLINE_COMMAND + " must contain Date indicated by \""
                    + DEADLINE_SPLIT_REGEX + "\".");
        }
        try {
            String[] deadlineDetails = description.split(DEADLINE_SPLIT_REGEX);
            String[] dates = deadlineDetails[1].split(" ");
            LocalDate date = LocalDate.parse(dates[0]);
            LocalTime time = LocalTime.parse(dates[1]);

            return new Command(CommandType.DEADLINE, deadlineDetails);
        } catch (Exception e) {
            throw new CommandException("OOPS!!! The Date format of a "
                    + DEADLINE_COMMAND + " must be yyyy-mm-dd hh:mm");
        }
    }
}
