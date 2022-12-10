package nextstep.subway.member.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Age {

    public static final int CHILD_START_AGE = 6;
    public static final int CHILD_END_AGE = 12;
    public static final int TEENAGER_START_AGE = 13;
    public static final int TEENAGER_END_AGE = 18;

    public static final int DEFAULT_AGE = 0;

    private int age;

    private Age(int age) {
        this.age = age;
    }

    protected Age() {
    }

    public static Age from(int age) {
        return new Age(age);
    }

    public static Age defaultAge() {
        return new Age(DEFAULT_AGE);
    }

    public int getAge() {
        return age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    public boolean isChild() {
        return isEqualsAfter(CHILD_START_AGE) && isEqualsBefore(CHILD_END_AGE);
    }

    public boolean isTeenager() {
        return isEqualsAfter(TEENAGER_START_AGE) && isEqualsBefore(TEENAGER_END_AGE);
    }

    private boolean isEqualsBefore(int age) {
        return this.age <= age;
    }

    private boolean isEqualsAfter(int age) {
        return this.age >= age;
    }
}
