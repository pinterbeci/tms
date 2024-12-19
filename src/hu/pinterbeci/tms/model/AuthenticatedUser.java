package hu.pinterbeci.tms.model;

public class AuthenticatedUser extends User{

    private byte [] hashedPassword;

    public AuthenticatedUser() {
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
