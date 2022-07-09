package nextstep.subway.fare.policy;

public class AgeDiscountCalculator implements DiscountCalculator {

    @Override
    public int calculate(int totalFare, int age) {
        if (age >= 13 && age < 19) {
            return (int) ((totalFare - 350) * 0.2);
        }

        if (age >= 6 && age < 13) {
            return (int) ((totalFare - 350) * 0.5);
        }

        return 0;
    }
}
