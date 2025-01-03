package hu.pinterbeci.tms.data;

import hu.pinterbeci.tms.enums.Priority;
import hu.pinterbeci.tms.enums.Role;
import hu.pinterbeci.tms.enums.Status;
import hu.pinterbeci.tms.errors.TMSException;
import hu.pinterbeci.tms.model.Task;
import hu.pinterbeci.tms.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class TestDataCreator {

    private TestDataCreator() {
    }

    public static void generateTestData() {
        DataFill.fillTestUsers();
        DataFill.fillTestTasks();
        final TMSDataHolder tmsDataHolder = new TMSDataHolder();
        tmsDataHolder.testSaveData(DataFill.testUsers, DataFill.testTasks);
    }

    static class DataFill {

        static List<User> testUsers = new ArrayList<>();

        static List<Task> testTasks = new ArrayList<>();

        static void fillTestUsers() {
            for (int i = 0; i < 15; i++) {
                final User user = new User();
                user.setName(DataCreator.generateRandomUsername());
                user.setEmail(DataCreator.generateRandomEmail(user.getName()));
                user.setRole(DataCreator.randomRole());
                testUsers.add(user);
            }
        }

        static void fillTestTasks() {
            if (testTasks.isEmpty()) {
                fillTestUsers();
            }
            for (int i = 0; i < 50; i++) {
                final Task task = new Task();
                task.setDueDate(DataCreator.generateRandomDueDate());
                task.setTitle(DataCreator.generateRandomTaskTitle());
                task.setPriority(DataCreator.randomPriority());
                task.setStatus(DataCreator.randomStatus());
                task.setDescription(DataCreator.generateRandomTaskDescription());
                task.setAssignedUser(DataCreator.getRandomUser(testUsers));

                testTasks.add(task);
            }
        }

        public List<User> getTestUsers() {
            return testUsers;
        }

        public List<Task> getTestTasks() {
            return testTasks;
        }
    }

    static class DataCreator {
        static final List<Role> ROLES = List.of(Role.ADMIN, Role.DEVELOPER, Role.TASK_VIEWER, Role.REGULAR_USER);

        static final List<Status> STATUS_LIST = List.of(Status.NOT_STARTED, Status.IN_PROGRESS, Status.COMPLETED);

        static final List<Priority> PRIORITY_LIST = List.of(Priority.LOW, Priority.MEDIUM, Priority.HIGH);

        static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

        static final String DIGITS = "0123456789";

        static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

        static final String ALPHABET = LOWER_CASE_LETTERS + UPPER_CASE_LETTERS;

        static final Random RANDOM = new Random();

        static StringBuilder stringBuilder;

        static Role randomRole() {
            final int randomRoleItemIndex = RANDOM.nextInt(ROLES.size());
            return ROLES.get(randomRoleItemIndex);
        }

        static Status randomStatus() {
            final int randomStatusItemIndex = RANDOM.nextInt(STATUS_LIST.size());
            return STATUS_LIST.get(randomStatusItemIndex);
        }

        static Priority randomPriority() {
            final int randomPriorityItemIndex = RANDOM.nextInt(PRIORITY_LIST.size());
            return PRIORITY_LIST.get(randomPriorityItemIndex);
        }

        static String generateRandomPassword() {
            stringBuilder = new StringBuilder();
            final String allCharacters = stringBuilder.append(UPPER_CASE_LETTERS)
                    .append(LOWER_CASE_LETTERS)
                    .append(DIGITS)
                    .append(SPECIAL_CHARACTERS)
                    .toString();

            stringBuilder = new StringBuilder();
            stringBuilder.append(UPPER_CASE_LETTERS.charAt(RANDOM.nextInt(UPPER_CASE_LETTERS.length())));
            stringBuilder.append(LOWER_CASE_LETTERS.charAt(RANDOM.nextInt(LOWER_CASE_LETTERS.length())));
            stringBuilder.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
            stringBuilder.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));

            for (int i = 4; i < 10; i++) {
                stringBuilder.append(allCharacters.charAt(RANDOM.nextInt(allCharacters.length())));
            }
            return shuffleString(stringBuilder.toString());
        }

        static String shuffleString(final String input) {
            final char[] characters = input.toCharArray();
            for (int i = characters.length - 1; i > 0; i--) {
                final int j = RANDOM.nextInt(i + 1);
                final char temp = characters[i];
                characters[i] = characters[j];
                characters[j] = temp;
            }
            return new String(characters);
        }

        static String generateRandomTaskDescription() {
            final String[] verbs = {"Analyze", "Develop", "Prepare", "Organize", "Coordinate", "Review", "Finalize"};
            final String[] objectives = {
                    "the monthly report",
                    "project deliverables",
                    "the client proposal",
                    "the team meeting agenda",
                    "the product roadmap",
                    "test cases",
                    "performance metrics"
            };
            final String[] contexts = {
                    "to meet the deadline",
                    "for better efficiency",
                    "to ensure quality",
                    "to achieve project goals",
                    "for presentation to stakeholders",
                    "as part of the quarterly review",
                    "in preparation for launch"
            };

            final String verb = verbs[RANDOM.nextInt(verbs.length)];
            final String objective = objectives[RANDOM.nextInt(objectives.length)];
            final String context = contexts[RANDOM.nextInt(contexts.length)];

            stringBuilder = new StringBuilder();
            return stringBuilder.append(verb)
                    .append(" ")
                    .append(objective)
                    .append(" ")
                    .append(context)
                    .append(".")
                    .toString();
        }

        static String generateRandomTaskTitle() {
            final String[] actions = {"Complete", "Review", "Plan", "Organize", "Create", "Design", "Implement"};
            final String[] objects = {"Report", "Presentation", "Database", "Schedule", "Prototype", "Survey", "Strategy"};
            final String[] contexts = {"for Q1", "by End of Day", "for Client Meeting",
                    "for Next Sprint", "with Team", "for Approval", "on Deadline"};

            final String action = actions[RANDOM.nextInt(actions.length)];
            final String object = objects[RANDOM.nextInt(objects.length)];
            final String context = contexts[RANDOM.nextInt(contexts.length)];

            stringBuilder = new StringBuilder();
            return stringBuilder
                    .append(action)
                    .append(" ")
                    .append(object)
                    .append(" ")
                    .append(context)
                    .toString();
        }

        static String generateRandomUsername() {
            final int usernameLength = RANDOM.nextInt(5) + 5;
            stringBuilder = new StringBuilder();
            for (int i = 0; i < usernameLength; i++) {
                stringBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            }
            return stringBuilder.toString();
        }

        static String generateRandomEmail(final String username) {
            final String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "example.com"};
            final String domain = domains[RANDOM.nextInt(domains.length)];
            stringBuilder = new StringBuilder();
            return stringBuilder.append(username).append("@").append(domain).toString();
        }

        static User getRandomUser(final List<User> users) {
            if (Objects.isNull(users)) {
                throw new TMSException("The user list must not be null!");
            }
            if (users.isEmpty()) {
                throw new TMSException("The user list must not be empty!");
            }

            final int randomIndex = RANDOM.nextInt(users.size());
            return users.get(randomIndex);
        }

        static LocalDateTime generateRandomDueDate() {
            LocalDateTime start = LocalDateTime.now().minusDays(5L);
            LocalDateTime end = start.plusMonths(1L);

            if (start.isAfter(end)) {
                throw new TMSException("Start date must be before end date.");
            }

            final long startEpoch = start.atZone(ZoneId.systemDefault()).toEpochSecond();
            final long endEpoch = end.atZone(ZoneId.systemDefault()).toEpochSecond();

            final long randomEpoch = startEpoch + Math.abs(RANDOM.nextLong()) % (endEpoch - startEpoch);

            return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneId.systemDefault().getRules().getOffset(start));
        }


    }

}
