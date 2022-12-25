package nextstep.subway.fare;

public class FareConstants {
    protected static final long ZERO_FARE = 0L;
    protected static final long BASE_FARE = 1_250L;
    protected static final long ADD_FARE = 100L;
    protected static final long DISCOUNT_FARE = 350L;
    protected static final float DISCOUNT_RATE_CHILDREN = 0.5F;
    protected static final float DISCOUNT_RATE_ADOLESCENT = 0.2F;
    protected static final int FIRST_FARE_SECTION_DELIMITER = 10;
    protected static final int SECOND_FARE_SECTION_DELIMITER = 50;
    protected static final int FIRST_FARE_SECTION_PER_DISTANCE = 5;
    protected static final int SECOND_FARE_SECTION_PER_DISTANCE = 8;
    protected static final int CHILD_AGE_START_SEPARATOR = 6;
    protected static final int CHILD_ADOLESCENT_AGE_BOUNDARY = 13;
    protected static final int ADOLESCENT_AGE_END_SEPARATOR = 19;

    private FareConstants() {}
}
