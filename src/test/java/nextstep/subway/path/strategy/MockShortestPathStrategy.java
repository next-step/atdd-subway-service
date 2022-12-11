package nextstep.subway.path.strategy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathStrategy;
import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

public class MockShortestPathStrategy implements PathStrategy {

    public MockShortestPathStrategy() {
    }

    @Override
    public PathFinder getShortPath(Station source, Station target) {
        return new PathFinder(Arrays.asList(source, target), 10);
    }
}
