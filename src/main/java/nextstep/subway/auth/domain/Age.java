package nextstep.subway.auth.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    private static final int MIN_CHILD_AGE = 6;
    private static final int MAX_CHILD_AGE = 12;
    private static final int MIN_TEENAGER_AGE = 13;
    private static final int MAX_TEENAGER_AGE = 18;

    @Column(name = "age")
    private int value;

    protected Age() {

    }

    private Age(int value) {
        this.value = value;
    }

    public boolean isChild() {
        return value >= MIN_CHILD_AGE && value <= MAX_CHILD_AGE;
    }

    public boolean isTeenager() {
        return value >= MIN_TEENAGER_AGE && value <= MAX_TEENAGER_AGE;
    }

    public static Age of(int value) {
        return new Age(value);
    }

    public int getValue() {
        return value;
    }
}
