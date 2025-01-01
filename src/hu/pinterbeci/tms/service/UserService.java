package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.annotations.TMSAllowedRoles;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.User;

import java.time.LocalDateTime;
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

    @Override
    protected User create(final User newUser) {
        if (Objects.isNull(newUser)) {
            final TMSException modelIsNull = new TMSException("New model creating is not possible, because of the model instance if null",
                    "NEW_MODEL_IS_NULL");
            modelIsNull.printTMSException();
            return null;
        }
        final boolean userNameAlreadyHave = getAll().stream()
                .anyMatch(user -> Objects.equals(user.getName(), newUser.getName()));

        if (userNameAlreadyHave) {
            final TMSException existWithUsername = new TMSException("The User is exits with this username. The username = " + newUser.getName(), "USER_IS_EXIST_WITH_USERNAME");
            existWithUsername.printTMSException();
            return null;
        }

        final boolean userEmailAlreadyHave = getAll().stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), newUser.getEmail()));

        if (userEmailAlreadyHave) {
            final TMSException existWithEmail = new TMSException("The User is exits with this e-mail. The username = " + newUser.getEmail(), "USER_IS_EXIST_WITH_EMAIL");
            existWithEmail.printTMSException();
            return null;
        }
        newUser.setCreatedDate(LocalDateTime.now());
        return saveNewItem(newUser);
    }

    @TMSAllowedRoles({Role.ADMIN})
    protected void assignRole(final String assignorId, final String assignedId, final Role role) {
        if (Objects.isNull(assignorId)) {
            throw new RuntimeException();
        }
        if (Objects.isNull(assignedId)) {
            throw new RuntimeException();
        }
        if (Objects.isNull(role)) {
            throw new RuntimeException();
        }

        findById(assignorId).ifPresent(user -> {
            if (!Objects.equals(user.getRole(), Role.ADMIN)) {
                throw new RuntimeException();
            }
        });
        findById(assignedId).ifPresent(user -> {
            user.setRole(role);
            update(user, user.getId());
        });
    }

    @TMSAllowedRoles({Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER})
    protected Optional<User> findByName(final String name) {
        return getAll().stream().filter(user -> Objects.nonNull(user.getId()) && Objects.equals(user.getName(), name)).findFirst();

    }
}