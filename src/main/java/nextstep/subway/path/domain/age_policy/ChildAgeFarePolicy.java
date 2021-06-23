package nextstep.subway.path.domain.age_policy;

public class ChildAgeFarePolicy implements AgeFarePolicy {

    private static final int DISCOUNT_PERCENT = 50;
    private static final int BASIC_DEDUCTION = 350;

    private int fare;

    public ChildAgeFarePolicy() {
    }

    @Override
    public int calculateByAge(int fare) {
        return (fare - BASIC_DEDUCTION) * DISCOUNT_PERCENT / 100;
    }
}
