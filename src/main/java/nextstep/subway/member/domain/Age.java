package nextstep.subway.member.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Age {
    // GTE(greater then or equlas)
    // LT(less than)
    private static final int CHILDREN_GTE = 6;
    private static final int CHILDREN_LT = 13;
    private static final int YOUTH_GTE = 13;
    private static final int YOUTH_LT = 19;

    private static final int AGE_DEDUCTION = 350;
    private static final double CHILDREN_AGE_DEDUCTION_PERCENT = 0.5;
    private static final double YOUTH_AGE_DEDUCTION_PERCENT = 0.8;

    @Column
    private int age;

    public Age(int age) {
        this.age = age;
    }

    public Age() {
    }

    public int getAge() {
        return age;
    }

    public int calculateAgeSale(int fare) {
        if (isChildren()) {
            return (int) Math.round((fare - AGE_DEDUCTION) * CHILDREN_AGE_DEDUCTION_PERCENT);
        }
        if (isYouth()) {
            return (int) Math.round((fare - AGE_DEDUCTION) * YOUTH_AGE_DEDUCTION_PERCENT);
        }
        return fare;
    }

    private boolean isYouth() {
        return age >= YOUTH_GTE && age < YOUTH_LT;
    }

    private boolean isChildren() {
        return age >= CHILDREN_GTE && age < CHILDREN_LT;
    }
}
