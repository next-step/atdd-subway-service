package nextstep.subway.fare.domain;

public class Fare {
    private static final String NOT_LESS_THAN_ZERO_FARE = "요금은 0원 보다 적을 수 없습니다.";
    private static final int FREE = 0;
    private final int fare;
    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int fare) {
        validateLessThanZeroFare(fare);
        return new Fare(fare);
    }

    private static void validateLessThanZeroFare(int fare) {
        if (fare < FREE) {
            throw new IllegalArgumentException(NOT_LESS_THAN_ZERO_FARE);
        }
    }

    public int get() {
        return this.fare;
    }
}
