package nextstep.subway.path.domain;

public enum AgeGroup {

    MINIMUM_TEENAGER_AGE(13),
    MAXIMUM_TEENAGER_AGE(19),
    MINIMUM_CHILD_AGE(6),
    MAXIMUM_CHILD_AGE(13);

    private final int age;

    AgeGroup(int age) {
        this.age = age;
    }

    public static boolean isTeenager(int age) {
        return age >= MINIMUM_TEENAGER_AGE.age && age < MAXIMUM_TEENAGER_AGE.age;
    }

    public static boolean isChild(int age) {
        return age >= MINIMUM_CHILD_AGE.age && age < MAXIMUM_CHILD_AGE.age;
    }
}
