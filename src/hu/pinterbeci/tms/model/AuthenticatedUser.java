package hu.pinterbeci.tms.model;

import hu.pinterbeci.tms.annotations.ConstructNewTMSInstance;

@ConstructNewTMSInstance
public class AuthenticatedUser extends User {

    private byte[] hashedPassword;

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
