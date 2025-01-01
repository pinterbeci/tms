package hu.pinterbeci.tms.errors;

public class TMSException extends Exception {

    private final String errorCode;

    public TMSException(String message) {
        super(message);
        this.errorCode = "UNKNOWN_ERROR";
    }

    public TMSException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TMSException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void printTMSException() {
        System.err.println("Error occurred: " + this.getMessage());
        System.err.println("Error Code: " + this.getErrorCode());
        System.err.println("Error Cause: " + this.getCause());
    }

    @Override
    public String toString() {
        return String.format("TMSException{errorCode='%s', message='%s'}", errorCode, getMessage());
    }
}
