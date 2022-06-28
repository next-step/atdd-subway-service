package nextstep.subway.path.domain.policy;

import java.util.function.Function;

public class TeenagerFarePolicy implements FarePolicy {
    public static final Function<Integer, Integer> TEENAGER_DISCOUNT = fare -> (int)(Math.ceil((fare - 350) * 4 / 5.0));

    @Override
    public int discount(int fare) {
        int resultFare = TEENAGER_DISCOUNT.apply(fare);

        if(validFare.test(resultFare)) {
            throw new IllegalArgumentException(FARE_INVALID_MSG);
        }

        return resultFare;
    }
}
