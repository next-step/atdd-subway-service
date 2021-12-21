package nextstep.subway.auth.domain;

import org.apache.commons.lang3.Range;

public class Age {

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

    public boolean isYouth() {
        return Range.between(13, 18).contains(age);
    }

    public boolean isChild() {
        return Range.between(6, 12).contains(age);
    }
}
