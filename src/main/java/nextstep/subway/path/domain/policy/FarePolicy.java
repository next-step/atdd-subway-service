package nextstep.subway.path.domain.policy;

import java.util.function.Predicate;

public interface FarePolicy {
    public static final String FARE_INVALID_MSG = "요금은 0원 이하일 수 없습니다.";

    Predicate<Integer> validFare = fare -> fare < 0;

    public int discount(int fare);
}
