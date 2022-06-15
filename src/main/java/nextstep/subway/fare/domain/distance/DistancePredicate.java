package nextstep.subway.fare.domain.distance;

import java.util.function.Predicate;

public interface DistancePredicate extends Predicate<Integer> {
    default boolean includeDistance(int distance) {
        return test(distance);
    }

}
