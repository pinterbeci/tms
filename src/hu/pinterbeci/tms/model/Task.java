package hu.pinterbeci.tms.model;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;
import hu.pinterbeci.tms.enums.Priority;
import hu.pinterbeci.tms.enums.Status;

import java.time.LocalDateTime;

@ConstructNewTMSInstance
public class Task extends BaseModel {

    private String title;

    private String description;

    private Priority priority;

    private LocalDateTime dueDate;

    private User assignedUser;

    private Status status;

    public Task() {
        this.status = Status.NOT_STARTED;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
