package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.errors.TMSException;
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
        if (Objects.isNull(user)) {
            throw new TMSException("Error occurred during user require role check: user data is null!");
        }
        return Objects.equals(getRoleByUserName(user.getName()), requiredRole);
    }

    private Role getRoleByUserName(final String userName) {
        final User user = this.authenticationService.getUserService().findByName(userName).orElse(null);
        if (Objects.isNull(user)) {
            throw new TMSException("User instance is null, please check the given user data!", "INVALID_USER_DUE_ROLE_CHECK");
        }

        final AuthenticatedUser authenticatedUser = this.authenticationService.getByAuthenticatedUsername(user.getName()).orElse(null);
        if (Objects.isNull(authenticatedUser)) {
            throw new TMSException("The User is not authenticated!", "NOT_AUTHENTICATED_USER");
        }

        if (!this.authenticationService.isAuthenticatedUserByName(authenticatedUser.getName(), authenticatedUser.getHashedPassword())) {
            throw new TMSException("The authentication by username and password FAILED!", "USERNAME_PW_AUTHENTICATION_FAILED");
        }
        return authenticatedUser.getRole();
    }
}
