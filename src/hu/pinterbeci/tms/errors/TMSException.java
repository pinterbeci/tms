package hu.pinterbeci.tms.errors;

public class TMSException extends RuntimeException {

    private final String errorCode;

    public TMSException(final String message) {
        super(message);
        this.errorCode = "UNKNOWN_ERROR";
    }

    public TMSException(final String message, final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TMSException(final String message, final String errorCode, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("TMSException {errorCode='%s', message='%s', cause='%s'}", getErrorCode(), getMessage(), getCause());
    }
}
