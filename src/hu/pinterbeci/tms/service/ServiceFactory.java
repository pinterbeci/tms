package hu.pinterbeci.tms.service;

public class ServiceFactory {

    private static final AuthenticationService AUTHENTICATION_SERVICE;

    private static final UserService USER_SERVICE;

    static {
        USER_SERVICE = UserService.getInstance();
        AUTHENTICATION_SERVICE = AuthenticationService.getInstance(USER_SERVICE);
    }

    public static UserService getUserService() {
        return USER_SERVICE;
    }

    public static AuthenticationService getAuthenticationService() {
        return AUTHENTICATION_SERVICE;
    }

    public TaskService getTaskService() {
        return TaskService.getInstance();
    }

    public RBACService getRBACService() {
        return RBACService.getInstance(getAuthenticationService());
    }
}
