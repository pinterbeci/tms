package hu.pinterbeci.tms.data;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.model.Task;
import hu.pinterbeci.tms.model.User;
import hu.pinterbeci.tms.service.ServiceFactory;
import hu.pinterbeci.tms.service.TMSReflectionCallService;
import hu.pinterbeci.tms.service.UserService;

import java.util.ArrayList;
import java.util.List;

@ConstructNewTMSInstance
public class TMSDataHolder {

    private List<User> savedUsers;

    private List<Task> savedTasks;

    public void testSaveData(final List<User> users, final List<Task> tasks) {
        savedUsers = new ArrayList<>(users);
        savedTasks = new ArrayList<>(tasks);

        //testing the TMS reflection feature
        final TMSReflectionCallService<User, UserService> tmsReflectionCallService =
                new TMSReflectionCallService<>(ServiceFactory.getUserService());
        tmsReflectionCallService.invoke("saveNewItemList", new Object[]{savedUsers}, Role.ADMIN, Boolean.class);
        final List<?> savedUsers = tmsReflectionCallService.invoke("getAll", new Object[]{}, Role.ADMIN, List.class);

        savedUsers.forEach(user -> {
            if (user instanceof User) {
                System.out.println(user);
            }
        });
    }

    public List<User> getSavedUsers() {
        return savedUsers;
    }

    public List<Task> getSavedTasks() {
        return savedTasks;
    }
}
