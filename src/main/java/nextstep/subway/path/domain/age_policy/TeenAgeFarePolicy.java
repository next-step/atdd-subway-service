package nextstep.subway.path.domain.age_policy;

public class TeenAgeFarePolicy implements AgeFarePolicy {

    private static final int DISCOUNT_PERCENT = 80;
    private static final int BASIC_DEDUCTION = 350;

    @Override
    public int calculateByAge(int fare) {
        return (fare - BASIC_DEDUCTION) * DISCOUNT_PERCENT / 100;
    }
}
