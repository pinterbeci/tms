package hu.pinterbeci.tms.model;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;
import hu.pinterbeci.tms.enums.Role;

@ConstructNewTMSInstance
public class User extends BaseModel {
    // todo
    // pakoljunk ra egy annotaciot, ami egyediseget vizsgal
    private String name;

    //todo
    // validáció legyen ennek is egy annotacioja
    private String email;

    private Role role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}