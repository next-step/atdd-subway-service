package nextstep.subway.path.domain.policy.fare.line;

import nextstep.subway.line.domain.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OverFareByLineStrategyFacade {
    private List<OverFareByLineStrategy> strategies = new ArrayList<>();

    public OverFareByLineStrategyFacade() {
        strategies.add(new DefaultOverFareByLineStrategy());
    }

    private Optional<OverFareByLineStrategy> findStrategy() {
        return Optional.ofNullable(strategies.get(0));
    }

    public int calculateOverFare(List<Line> lines) {
        int overFare = 0;
        Optional<OverFareByLineStrategy> findStrategy = findStrategy();
        if (findStrategy.isPresent()) {
            overFare = findStrategy.get().calculateOverFare(lines);
        }
        return overFare;
    }
}
