package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.model.User;

import java.util.Objects;
import java.util.Optional;

public class UserService extends BaseService<User> {

    private static UserService instance;

    private UserService() {
    }

    protected static UserService getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (UserService.class) {
                if (Objects.isNull(instance)) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public void assignRole(final String assignorId, final String assignedId, final Role role) {
        if (Objects.isNull(assignorId)) {
            throw new RuntimeException();
        }
        if (Objects.isNull(assignedId)) {
            throw new RuntimeException();
        }
        if (Objects.isNull(role)) {
            throw new RuntimeException();
        }
        // todo jogosultsag kezeles, kvazi keycloak role,
        findById(assignorId).ifPresent(user -> {
            if (!Objects.equals(user.getRole(), Role.ADMIN)) {
                throw new RuntimeException();
            }
        });
        findById(assignedId).ifPresent(user -> user.setRole(role));
    }

    public Optional<User> findByName(final String name) {
        return getAll().stream().filter(user -> Objects.nonNull(user.getId()) && Objects.equals(user.getName(), name)).findFirst();

    }
}