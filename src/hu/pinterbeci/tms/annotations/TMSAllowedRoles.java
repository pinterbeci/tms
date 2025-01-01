package hu.pinterbeci.tms.annotations;

import hu.pinterbeci.tms.enums.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TMSAllowedRoles {
    Role[] value();
}