package nextstep.subway.path.domain.policy.fare.distance;

import nextstep.subway.path.domain.ShortestDistance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OverFareByDistanceStrategyFacade {
    private List<OverFareByDistanceStrategy> strategies = new ArrayList<>();

    public OverFareByDistanceStrategyFacade() {
        strategies.add(new NotMoreThan10KmOverFareByDistanceStrategy());
        strategies.add(new GraterThan10KmNotMoreThan50KmOverFareByDistanceStrategy());
        strategies.add(new GraterThan50KmOverFareByDistanceStrategy());
    }

    private Optional<OverFareByDistanceStrategy> findStrategy(ShortestDistance distance) {
        return strategies.stream()
                .filter(strategy -> strategy.isAvailable(distance))
                .findFirst();
    }

    public int calculateOverFare(ShortestDistance distance) {
        int overFare = 0;
        Optional<OverFareByDistanceStrategy> findStrategy = findStrategy(distance);
        if (findStrategy.isPresent()) {
            overFare = findStrategy.get().calculateOverFare(distance);
        }

        return overFare;
    }
}
