package hu.pinterbeci.tms.service;

import hu.pinterbeci.tms.model.AuthenticatedUser;
import hu.pinterbeci.tms.model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AuthenticationService {

    private final List<AuthenticatedUser> authenticatedUsers;

    private final UserService userService;

    public AuthenticationService(final UserService userService) {
        this.userService = userService;
        authenticatedUsers = new ArrayList<>();
    }

    public boolean authenticateUserByAndPW(final String username, final String password) {
        if (Objects.isNull(username))
            return false;
        if (Objects.isNull(password))
            return false;

        final User userByName = this.userService.findByName(username).orElse(null);

        if (Objects.isNull(userByName))
            return false;

        final AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setRole(userByName.getRole());
        authenticatedUser.setName(username);
        authenticatedUser.setHashedPassword(passwordHash(password));
        return authenticatedUsers.add(authenticatedUser);
    }

    public boolean isAuthenticatedUserByName(final String username, final String password) {
        final byte[] hashedPassword = passwordHash(password);

        final AuthenticatedUser authenticatedUser = authenticatedUsers.stream()
                .filter(user -> Objects.equals(username, user.getName()) &&
                        Arrays.equals(hashedPassword, user.getHashedPassword()))
                .findFirst()
                .orElse(null);
        return Objects.nonNull(authenticatedUser);
    }

    private byte[] passwordHash(final String passwordStr) {
        try {
            final byte[] salt = new byte[16];
            final MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.digest(salt);
            return digest.digest(passwordStr.getBytes(StandardCharsets.UTF_8));
        } catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
