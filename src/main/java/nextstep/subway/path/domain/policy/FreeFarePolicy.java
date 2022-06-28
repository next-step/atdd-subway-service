package nextstep.subway.path.domain.policy;

public class FreeFarePolicy implements FarePolicy {
    public static final int FREE_FARE = 0;

    @Override
    public int discount(int fare) {
        if(validFare.test(fare)) {
            throw new IllegalArgumentException(FARE_INVALID_MSG);
        }
        return FREE_FARE;
    }
}
