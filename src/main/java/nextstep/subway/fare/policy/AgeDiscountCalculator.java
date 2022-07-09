package nextstep.subway.fare.policy;

public class AgeDiscountCalculator implements DiscountCalculator {

    @Override
    public int calculate(int totalFare, int age) {
        AgeType ageType = AgeType.of(age);
        if (ageType.isTeenagers(ageType)) {
            return (int) ((totalFare - 350) * 0.2);
        }

        if (ageType.isChildren(ageType)) {
            return (int) ((totalFare - 350) * 0.5);
        }

        return 0;
    }
}
