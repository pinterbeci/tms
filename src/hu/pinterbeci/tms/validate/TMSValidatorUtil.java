package hu.pinterbeci.tms.validate;

import hu.pinterbeci.tms.errors.TMSException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TMSValidatorUtil {

    private TMSValidatorUtil() {
    }

    public static boolean validateEmailSetter(final String email) {
        final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new TMSException("The email you want to set is invalid! The email = " + email, "INVALID_EMAIL");
        }
        return true;
    }

    public static boolean validateTaskTitle(final String taskTitle) {
        if (Objects.isNull(taskTitle)) {
            throw new TMSException("The task title is null!", "INVALID_TASK_TITLE_NULL");
        }

        if (Objects.equals("", taskTitle)) {
            throw new TMSException("The task title is empty!", "INVALID_TASK_TITLE_EMPTY");
        }

        if (taskTitle.length() < 10) {
            throw new TMSException("The task title length is less than 10 character!", "INVALID_TASK_TITLE_LENGTH");
        }
        return true;
    }
}
