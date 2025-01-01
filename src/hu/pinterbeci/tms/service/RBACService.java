package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.model.AuthenticatedUser;
import hu.pinterbeci.tms.model.User;

import java.util.Objects;

public class RBACService {

    private static RBACService instance;

    private final AuthenticationService authenticationService;

    private RBACService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public static RBACService getInstance(final AuthenticationService authenticationService) {
        if (Objects.isNull(instance)) {
            synchronized (RBACService.class) {
                if (Objects.isNull(instance)) {
                    instance = new RBACService(authenticationService);
                    return instance;
                }
            }

        }

        return instance;
    }

    protected boolean hasRole(final User user, final Role requiredRole) {
        if (Objects.isNull(user))
            return false;
        return Objects.equals(getRoleByUserName(user.getName()), requiredRole);
    }

    private Role getRoleByUserName(final String userName) {
        final User user = this.authenticationService.getUserService().findByName(userName).orElse(null);
        if (Objects.isNull(user)) {
            throw new RuntimeException();
        }

        final AuthenticatedUser authenticatedUser =
                this.authenticationService.getByAuthenticatedUsername(user.getName()).orElse(null);
        if (Objects.isNull(authenticatedUser)) {
            throw new RuntimeException();
        }
        if (!this.authenticationService
                .isAuthenticatedUserByName(authenticatedUser.getName(), authenticatedUser.getHashedPassword())) {
            throw new RuntimeException();
        }
        return authenticatedUser.getRole();
    }
}
