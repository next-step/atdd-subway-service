package nextstep.subway.path.domain;

import nextstep.subway.path.domain.strategy.PathStrategy;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    private PathFinder() {
    }

    public static List<Station> find(PathStrategy pathStrategy) {
        return pathStrategy.find();
    }
}
