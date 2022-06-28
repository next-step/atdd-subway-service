package nextstep.subway.path.domain.policy;

public class AllFarePolicy implements FarePolicy {

    @Override
    public int discount(int fare) {
        if(validFare.test(fare)) {
            throw new IllegalArgumentException(FARE_INVALID_MSG);
        }
        return fare;
    }
}
