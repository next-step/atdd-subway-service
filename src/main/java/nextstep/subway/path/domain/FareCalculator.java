package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;

public class FareCalculator {

    private static final int BASE_FARE_DISTANCE = 10;
    private static final int EXTRA_FARE_DISTANCE = 50;
    private static final int BASIC_FARE = 1_250;
    private static final int EXTRA_FARE = 100;
    private static final int DISTANCE1_TO_CHARGE_EXTRA_FARE = 5;
    private static final int DISTANCE2_TO_CHARGE_EXTRA_FARE = 8;

    private static final int DISCOUNT_DEDUCTIBLE_AMOUNT = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double YOUTH_DISCOUNT_RATE = 0.2;

    public FareCalculator() {
    }

    public static Fare calculateFare(Path path, Member member) {
        Fare fare = calculateExtraFare(path.getDistance());
        fare.addFare(calculateExtraFare(path.getLineList()));
        fare = calculateDiscount(fare, member);
        return fare;
    }

    public static Fare calculateExtraFare(int distance) {
        Fare fare = new Fare(BASIC_FARE);
        if (distance > EXTRA_FARE_DISTANCE) {
            return fare.addFare(calculateOverBaseFare(EXTRA_FARE_DISTANCE - BASE_FARE_DISTANCE))
                .addFare(calculateOverExtraFare(distance - EXTRA_FARE_DISTANCE));
        }
        if (distance > BASE_FARE_DISTANCE) {
            return fare.addFare(calculateOverBaseFare(distance - BASE_FARE_DISTANCE));
        }
        return fare;
    }

    public static Fare calculateExtraFare(List<Line> lineList) {
        return new Fare(lineList
            .stream()
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0));
    }

    public static Fare calculateDiscount(Fare fare, Member member) {
        if (member.isOfChildAge()) {
            return calculateChildDiscount(fare);
        }
        if (member.isOfYouthAge()) {
            return calculateYouthDiscount(fare);
        }
        return fare;
    }

    private static Fare calculateOverBaseFare(int distance) {
        return new Fare((int) ((Math.ceil((distance - 1) / DISTANCE1_TO_CHARGE_EXTRA_FARE) + 1)
            * EXTRA_FARE));
    }

    private static Fare calculateOverExtraFare(int distance) {
        return new Fare((int) ((Math.ceil((distance - 1) / DISTANCE2_TO_CHARGE_EXTRA_FARE) + 1)
            * EXTRA_FARE));
    }

    private static Fare calculateChildDiscount(Fare fareObj) {
        int fare = fareObj.getValue();
        return new Fare((int) (fare - (fare - DISCOUNT_DEDUCTIBLE_AMOUNT) * CHILDREN_DISCOUNT_RATE));
    }

    private static Fare calculateYouthDiscount(Fare fareObj) {
        int fare = fareObj.getValue();
        return new Fare((int) (fare - (fare - DISCOUNT_DEDUCTIBLE_AMOUNT) * YOUTH_DISCOUNT_RATE));
    }
}
