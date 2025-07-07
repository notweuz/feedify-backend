package ru.ntwz.feedify.constant;

public class ValidationConstant {
    public static class Length {
        public static final int NAME_MIN = 3;
        public static final int NAME_MAX = 20;

        public static final int DESCRIPTION_MIN = 0;
        public static final int DESCRIPTION_MAX = 200;

        public static final int PASSWORD_MIN = 8;
        public static final int PASSWORD_MAX = 32;

        public static final int POST_CONTENT_MIN = 0;
        public static final int POST_CONTENT_MAX = 1000;
    }

    public static class Message {
        public static final String INCORRECT_FORMAT = " is in incorrect format";

        public static final String REQUIRED = " is required";

        public static final String SHOULD_BE_POSITIVE_OR_ZERO = " should be positive or zero";

        public static final String SHOULD_BE_POSITIVE = " should be positive";

        public static final String WRONG_LENGTH = " should be between {min} and {max} characters";
    }

    public static class Regex {
        public static final String NAME_REGEX = "^[A-Za-z0-9_-]+$";
    }
}
