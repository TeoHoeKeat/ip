package fakebot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TaskListTest {

    private String description1 = "test1";
    private String description2 = "test2";
    private String startDateString = "2000-10-01";
    private String startTimeString = "01:04";
    private String endDateString = "2011-10-01";
    private String endTimeString = "04:04";

    @Test
    void getTask_equal() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        Task test2 = new Todo(description2);
        taskList.addTask(test1);
        taskList.addTask(test2);
        assertEquals(test1, taskList.getTask(0), "Task index do not match");
        assertEquals(test2, taskList.getTask(1), "Task index do not match");
    }

    @Test
    void addTask_success() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        taskList.addTask(test1);
        assertEquals(1, taskList.getSize(), "Task is not added");
    }

    @Test
    void removeTask_success() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        taskList.addTask(test1);
        taskList.removeTask(0);
        assertEquals(0, taskList.getSize(), "Task is not removed");
    }

    @Test
    void getSize_success() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        taskList.addTask(test1);
        taskList.addTask(test1);
        taskList.addTask(test1);
        assertEquals(3, taskList.getSize(), "Task List size does not match");
    }

    @Test
    void find_success() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        Task test2 = new Todo(description2);
        taskList.addTask(test1);
        taskList.addTask(test2);
        taskList.addTask(test1);
        taskList.addTask(test2);
        taskList.addTask(test1);
        taskList.addTask(test2);
        taskList.addTask(test1);
        taskList.addTask(test2);
        taskList.addTask(test1);
        taskList.addTask(test1);
        assertEquals(6, taskList.findTasks("est1").size(), "Task size found does not match");
        assertEquals(4, taskList.findTasks("est2").size(), "Task size found does not match");
    }

    @Test
    void containTask_true() {
        TaskList taskList = new TaskList(new ArrayList<>());
        Task test1 = new Todo(description1);
        Task test2 = new Todo(description1);
        Task test3 = new Todo(description1);
        Task test4 = new Todo(description1);
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalDate endDate = LocalDate.parse(endDateString);
        LocalTime endTime = LocalTime.parse(endTimeString);
        Deadline test5 = new Deadline(description1, startDate, startTime);
        Deadline test6 = new Deadline(description1, startDate, startTime);
        Event test7 = new Event(description1, startDate, startTime, endDate, endTime);
        Event test8 = new Event(description1, startDate, startTime, endDate, endTime);
        taskList.addTask(test1);
        taskList.addTask(test3);
        taskList.addTask(test5);
        taskList.addTask(test7);
        assertTrue(taskList.containTask(test2), "Task does not contain in list");
        assertTrue(taskList.containTask(test4), "Task does not contain in list");
        assertTrue(taskList.containTask(test6), "Task does not contain in list");
        assertTrue(taskList.containTask(test8), "Task does not contain in list");
    }
}
