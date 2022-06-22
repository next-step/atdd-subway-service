package nextstep.subway.auth.domain;

public class Age {

    private final static int MINIMUM_AGE_OF_CHILD = 6;
    private final static int MAXIMUM_AGE_OF_CHILD = 13;

    private final static int MINIMUM_AGE_OF_YOUTH = 13;
    private final static int MAXIMUM_AGE_OF_YOUTH = 19;

    private final int age;

    protected Age(int age) {
        this.age = age;
    }

    public static Age of(int age) {
        return new Age(age);
    }

    public int getAge() {
        return age;
    }

    public boolean isChild() {
        return age >= MINIMUM_AGE_OF_CHILD && age < MAXIMUM_AGE_OF_CHILD;
    }

    public boolean isYouth() {
        return age >= MINIMUM_AGE_OF_YOUTH && age < MAXIMUM_AGE_OF_YOUTH;
    }
}
