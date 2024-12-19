package hu.pinterbeci.tms.service;

public class ServiceFactory {

    public static UserService getUserService() {
        return UserService.getInstance();
    }

    public static AuthenticationService getAuthenticationService() {
        return new AuthenticationService(getUserService());
    }

    public TaskService getTaskService(){
        return TaskService.getInstance();
    }
}
