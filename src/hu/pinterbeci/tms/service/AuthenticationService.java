package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.AuthenticatedUser;
import hu.pinterbeci.tms.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuthenticationService {

    private static AuthenticationService instance;

    private final List<AuthenticatedUser> authenticatedUsers;

    private final UserService userService;

    private AuthenticationService(final UserService userService) {
        this.userService = userService;
        authenticatedUsers = new ArrayList<>();
    }

    protected static AuthenticationService getInstance(final UserService userService) {
        if (Objects.isNull(instance)) {
            synchronized (AuthenticationService.class) {
                if (Objects.isNull(instance)) {
                    instance = new AuthenticationService(userService);
                }
            }
        }
        return instance;
    }

    protected boolean authenticateUserByAndPW(final String username, final String password) {
        if (Objects.isNull(username)) {
            throw new TMSException("Error occurred during user authentication: username is null!");
        }
        if (Objects.isNull(password)) {
            throw new TMSException("Error occurred during user authentication: password is null!");
        }

        final User userByName = this.userService.findByName(username).orElse(null);

        if (Objects.isNull(userByName)) {
            throw new TMSException("Error occurred during user authentication: user data not found!");
        }

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setRole(userByName.getRole());
        authenticatedUser.setName(username);
        authenticatedUser.setHashedPassword(passwordHash(password));
        return authenticatedUsers.add(authenticatedUser);
    }

    protected boolean isAuthenticatedUserByName(final String username, final String password) {
        final byte[] hashedPassword = passwordHash(password);
        return isAuthenticatedUserByName(username, hashedPassword);
    }

    protected boolean isAuthenticatedUserByName(final String username, final byte[] hashedPassword) {
        final AuthenticatedUser authenticatedUser = authenticatedUsers.stream()
                .filter(user -> Objects.equals(username, user.getName()) &&
                        Arrays.equals(hashedPassword, user.getHashedPassword()))
                .findFirst()
                .orElse(null);
        return Objects.nonNull(authenticatedUser);
    }

    protected boolean isAuthenticatedUserByName(final String userName) {
        if (Objects.isNull(userName)) {
            throw new TMSException("Error occurred during authentication check by username: username is null!");
        }
        return Objects.isNull(
                getByAuthenticatedUsername(userName)
                        .orElse(null)
        );
    }

    protected byte[] passwordHash(final String passwordStr) {
        try {
            final byte[] salt = new byte[16];
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.digest(salt);
            return digest.digest(passwordStr.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception exception) {
            throw new TMSException("Password hash method not successfully and thrown an exception",
                    "INVALID_PASSWORD_HASH",
                    exception);
        }
    }

    protected Optional<AuthenticatedUser> getByAuthenticatedUsername(final String userName) {
        return this.authenticatedUsers.stream()
                .filter(user -> Objects.nonNull(user.getHashedPassword())
                        && Objects.equals(user.getName(), userName))
                .findFirst();
    }

    protected UserService getUserService() {
        return userService;
    }
}
