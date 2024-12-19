package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.enums.Priority;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.enums.Status;
import hu.pinterbeci.tms.model.Task;
import hu.pinterbeci.tms.model.User;

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

    public void assignTask(final String id, final User user) {
        if (Objects.isNull(id))
            return;
        if (Objects.isNull(user))
            return;

        findById(id).ifPresent(task -> {
            task.setAssignedUser(user);
            update(task, id);
        });
    }

    public void assignTaskToDeveloper(final String id, final User user) {
        findById(id).ifPresent(task -> {
                    if (Objects.nonNull(user)) {
                        if (Objects.equals(Role.DEVELOPER, user.getRole())) {
                            task.setStatus(Status.IN_PROGRESS);
                            task.setAssignedUser(user);
                            update(task, id);
                        }
                    }
                }
        );
    }

    public void assignTaskToViewer(final String id, final User user) {
        findById(id).ifPresent(task -> {
            if (Objects.nonNull(user)) {
                if (Objects.equals(user.getRole(), Role.TASK_VIEWER)) {
                    task.setAssignedUser(user);
                    task.setStatus(Status.COMPLETED);
                    update(task, id);
                }
            }
        });
    }

    public List<Task> sortByPriorityAndDueDate() {
        return getAll().stream()
                .sorted(Comparator.comparing(Task::getPriority).thenComparing(Task::getDueDate))
                .toList();
    }

    public List<Task> filterByStatusAndPriority(final Status status, final Priority priority) {
        return getAll().stream()
                .filter(task -> Objects.equals(task.getStatus(), status) && Objects.equals(task.getPriority(), priority))
                .toList();
    }
}