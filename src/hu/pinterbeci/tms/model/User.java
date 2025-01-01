package hu.pinterbeci.tms.model;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.validate.TMSValidatorUtil;

@ConstructNewTMSInstance
public class User extends BaseModel {
    private String name;

    private String email;

    private Role role;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        if (TMSValidatorUtil.validateEmailSetter(email)) {
            this.email = email;
        }
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}