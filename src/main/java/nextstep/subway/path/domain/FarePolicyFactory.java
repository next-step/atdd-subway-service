package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class FarePolicyFactory {

    private FarePolicyFactory() {
    }

    public static FarePolicy of(Lines lines, ShortestPath shortestPath) {
        List<Station> path = shortestPath.getPath();
        int distance = shortestPath.getDistance();

        FarePolicy lineSurchargePolicy = new LineSurchargePolicy(() -> 0, lines.getLinesInShortestPath(path));
        return new DistanceFarePolicy(lineSurchargePolicy, new Distance(distance));
    }
}
