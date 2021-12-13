package nextstep.subway.auth.domain;

public class Age {

    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final int MIN_TEENAGER_AGE = 13;
    private static final int MAX_TEENAGER_AGE = 18;

    private int age;

    public Age(int age) {
        this.age = age;
    }

    public boolean isChild() {
        return this.age >= MIN_CHILD_AGE && this.age <= MAX_CHILD_AGE;
    }

    public boolean isTeenager() {
        return this.age >= MIN_TEENAGER_AGE && this.age <= MAX_TEENAGER_AGE;
    }

    public int getAge() {
        return age;
    }
}
