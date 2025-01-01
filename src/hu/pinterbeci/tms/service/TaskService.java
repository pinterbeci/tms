package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.annotations.TMSAllowedRoles;
import hu.pinterbeci.tms.enums.HistoryStatus;
import hu.pinterbeci.tms.enums.Priority;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.enums.Status;
import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.Task;
import hu.pinterbeci.tms.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TaskService extends BaseService<Task> {

    private static TaskService instance;

    private TaskService() {
    }

    protected static TaskService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (TaskService.class) {
                if (Objects.isNull(instance)) {
                    instance = new TaskService();
                    return instance;
                }
            }
        }
        return instance;
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected void assignTask(final String id, final User user) {
        if (Objects.isNull(id)) {
            final TMSException invalidTaskAssign = new TMSException("Task is is null! Assign is not possible!", "INVALID_TASK_ASSIGN");
            invalidTaskAssign.printTMSException();
            return;
        }
        if (Objects.isNull(user)) {
            final TMSException invalidTaskAssignToUser = new TMSException("User is null, cannot assign task to them!", "INVALID_TASK_ASSIGN_TO_USER");
            invalidTaskAssignToUser.printTMSException();
            return;
        }

        findById(id).ifPresent(task -> {
            task.setAssignedUser(user);
            update(task, id);
            task.setHistoryStatus(HistoryStatus.UPDATING);
            fillTaskHistoryData(task);
        });
    }

    @TMSAllowedRoles({Role.ADMIN, Role.TASK_VIEWER})
    protected void assignTaskToDeveloper(final String id, final User user) {
        findById(id).ifPresent(task -> {
            userRoleAppropriateCheck(user, Role.DEVELOPER);
            task.setStatus(Status.IN_PROGRESS);
            task.setAssignedUser(user);
            update(task, id);
            fillTaskHistoryData(task);
        });
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER})
    protected void assignTaskToViewer(final String id, final User user) {
        findById(id).ifPresent(task -> {
            userRoleAppropriateCheck(user, Role.TASK_VIEWER);
            task.setAssignedUser(user);
            task.setStatus(Status.COMPLETED);
            update(task, id);
            fillTaskHistoryData(task);
        });
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER})
    protected List<Task> sortByPriorityAndDueDate() {
        return getAll().stream()
                .sorted(Comparator.comparing(Task::getPriority).thenComparing(Task::getDueDate))
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER})
    protected List<Task> filterByStatusAndPriority(final Status status, final Priority priority) {
        return getAll().stream()
                .filter(task -> Objects.equals(task.getStatus(), status) && Objects.equals(task.getPriority(), priority))
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER})
    protected void startTask(final String id) {
        changeTaskStatus(id, Status.IN_PROGRESS);
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER})
    protected void closeTask(final String id) {
        changeTaskStatus(id, Status.COMPLETED);
    }

    @TMSAllowedRoles({Role.ADMIN, Role.TASK_VIEWER, Role.DEVELOPER})
    protected List<Task> completedTasks() {
        return this.getAll().stream()
                .filter(task -> Objects.equals(task.getStatus(), Status.COMPLETED))
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN, Role.TASK_VIEWER, Role.DEVELOPER})
    protected List<Task> pendingTasks() {
        return this.getAll().stream()
                .filter(task -> Objects.equals(task.getStatus(), Status.IN_PROGRESS))
                .toList();
    }

    @TMSAllowedRoles({Role.ADMIN, Role.TASK_VIEWER, Role.DEVELOPER})
    protected List<Task> overdueTasks() {
        return this.getAll().stream()
                .filter(task -> task.getDueDate().isBefore(LocalDateTime.now()))
                .toList();
    }

    private void changeTaskStatus(final String id, final Status status) {
        findById(id).ifPresent(task -> {
            task.setStatus(status);
            update(task, id);
            fillTaskHistoryData(task);
        });
    }

    private void userRoleAppropriateCheck(final User user, final Role necessaryRole) {
        if (Objects.isNull(user)) {
            final TMSException invalidTaskAssignToUser = new TMSException("User is null, cannot assign task to them!", "INVALID_TASK_ASSIGN_TO_USER");
            invalidTaskAssignToUser.printTMSException();
            return;
        }
        if (!Objects.equals(user.getRole(), necessaryRole)) {
            final TMSException invalidTaskAssignToUser = new TMSException("The Role of the assigned user is not appropriate!", "INVALID_TASK_ASSIGN_TO_USER");
            invalidTaskAssignToUser.printTMSException();
        }
    }

}