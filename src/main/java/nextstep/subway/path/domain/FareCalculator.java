package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {

    private final FarePolicy farePolicy;

    public FareCalculator(LoginMember loginMember) {
        this.farePolicy = FarePolicy.get(loginMember.getAge());
    }

    public Fare calculate(Path path) {
        return this.calculate(path.getDistance(), path.getSurcharge());
    }

    protected Fare calculate(int distance, int lineSurcharge) {
        return calculate(distance, Fare.wonOf(lineSurcharge));
    }

    protected Fare calculate(int distance, Fare lineSurcharge) {
        Fare defaultFare = Fare.wonOf(farePolicy.getDefaultAmount());
        if (defaultFare.isFree()) {
            return defaultFare;
        }
        DistanceSurchargeCalculator calculator = DistanceSurchargeCalculator.get(distance);

        return defaultFare
            .plus(lineSurcharge)
            .plus(calculator.calculate(distance))
            .minus(farePolicy.getDiscountAmount())
            .applyDiscountRate(farePolicy.getDiscountRate());
    }
}
