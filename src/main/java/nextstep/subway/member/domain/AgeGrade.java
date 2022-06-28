package nextstep.subway.member.domain;

public enum AgeGrade {

    GENERAL("일반"),
    YOUTH("청소년"),
    CHILDREN("어린이");

    private static final int YOUTH_MIN_AGE = 13;
    private static final int YOUTH_MAX_AGE = 18;
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_MAX_AGE = 12;
    private final String description;

    AgeGrade(String description) {
        this.description = description;
    }

    public static AgeGrade getGrade(Integer age) {
        if (isChildren(age)) {
            return CHILDREN;
        }
        if (isYouth(age)) {
            return YOUTH;
        }
        return CHILDREN;
    }

    private static boolean isChildren(Integer age) {
        return CHILDREN_MIN_AGE <= age && age <= CHILDREN_MAX_AGE;
    }

    private static boolean isYouth(Integer age) {
        return YOUTH_MIN_AGE <= age && age <= YOUTH_MAX_AGE;
    }
}
