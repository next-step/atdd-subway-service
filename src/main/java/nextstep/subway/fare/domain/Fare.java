package nextstep.subway.fare.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1_250;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(final int distance, final int lineExtraCharge, final int age) {
        final int totalCharge = totalCharge(lineExtraCharge, distance);
//        }
        return new Fare(AgeFarePolicy.ofDiscount(age, totalCharge));
    }

    private static int totalCharge(final int extraCharge, final int distance) {
        return DEFAULT_FARE + extraCharge + DistanceFarePolicy.ofExtraCharge(distance);
    }

    public int getValue() {
        return value;
    }

}
