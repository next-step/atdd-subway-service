package nextstep.subway.fare.domain;

public class FareConstant {
    public static final int BASIC_FARE = 1_250;
    public static final int MIN_DISTANCE_OF_BASIC_FARE_SECTION = 0;
    public static final int MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION = 10;
    public static final int MIN_DISTANCE_OF_SECOND_OVER_FARE_SECTION = 50;
    public static final int NO_OVER_FARE = 0;
    public static final double PER_5_KM = 5.0;
    public static final double PER_8_KM = 8.0;
    public static final int OVER_FARE_BY_DISTANCE = 100;

    private FareConstant() {
    }
}
