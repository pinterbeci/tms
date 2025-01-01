package hu.pinterbeci.tms.validate;

import hu.pinterbeci.tms.errors.TMSException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TMSValidatorUtil {

    private TMSValidatorUtil() {
    }

    public static void validateEmailSetter(final String email) {
        final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            final TMSException invalidEmail = new TMSException("The email you want to set is invalid! The email = " + email, "INVALID_EMAIL");
            invalidEmail.printTMSException();
        }
    }
}
