package fakebot.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;


class DeadlineTest {
    private String taskName = "Test";
    private String dateString = "2000-10-01";
    private String timeString = "01:04";

    @Test
    public void getTaskName_equal() {
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        Deadline deadline = new Deadline(taskName, date, time);
        assertEquals(taskName, deadline.getTaskName(), "Task Name does not match");
    }

    @Test
    public void markComplete_success() {
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        Deadline deadline = new Deadline(taskName, date, time);
        deadline.markComplete();
        assertEquals(true, deadline.isComplete(), "Task not completed");
    }

    @Test
    public void getDeadlineDate_equal() {
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        Deadline deadline = new Deadline(taskName, date, time);
        assertEquals(dateString, deadline.getDeadlineDate(), "Deadline Date does not match");
    }

    @Test
    public void getDeadlineTime_equal() {
        LocalDate date = LocalDate.parse(dateString);
        LocalTime time = LocalTime.parse(timeString);
        Deadline deadline = new Deadline(taskName, date, time);
        assertEquals(timeString, deadline.getDeadlineTime(), "Deadline Time does not match");
    }
}
