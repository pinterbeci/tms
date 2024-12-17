package hu.pinterbeci.tms.service;

import java.util.Objects;

import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.model.User;

public class UserService extends BaseService<User> {

    public UserService() {
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

        findById(assignorId).ifPresent(
                user -> {
                    if (!Objects.equals(user.getRole(), Role.ADMIN)) {
                        throw new RuntimeException();
                    }
                }
        );
        findById(assignorId).ifPresent(user -> user.setRole(role));
    }
}