package hu.pinterbeci.tms.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import hu.pinterbeci.tms.enums.Priority;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.enums.Status;
import hu.pinterbeci.tms.model.Task;
import hu.pinterbeci.tms.model.User;

public class TaskService extends BaseService<Task> {
    // todo
    //  hibaüzenet, ha nem task viewer, vagy null-os a user, kvázi egy else ág --> egy hibaüzenet

    public void assignTask(final String id, final User user) {
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

    public void completeTask(final String id, final User user) {
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