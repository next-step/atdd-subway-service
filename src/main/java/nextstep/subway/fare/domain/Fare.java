package nextstep.subway.fare.domain;

public class Fare {
    private static final int MIN = 0;
    private static final int BASIC = 1_250;
    private static final int FIRST_MAX_SURCHARGE = 800;
    public static final Fare FREE = new Fare(MIN);
    public static final Fare BASIC_FARE = new Fare(BASIC);
    public static final Fare FIRST_MAX_FARE = new Fare(BASIC + FIRST_MAX_SURCHARGE);

    private final int value;

    public Fare(int value) {
        validateFare(value);
        this.value = value;
    }

    private void validateFare(int value) {
        if (value < MIN) {
            throw new IllegalArgumentException("요금은 0원 이상이어야 합니다.");
        }
    }

    public Fare add(Fare fare) {
        return new Fare(value + fare.value);
    }

    public int value() {
        return value;
    }
}
