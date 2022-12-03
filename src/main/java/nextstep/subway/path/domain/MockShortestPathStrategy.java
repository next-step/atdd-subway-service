package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Arrays;

public class MockShortestPathStrategy implements PathStrategy {
    @Override
    public PathFinder getShortPath(Station source, Station target) {
        return new PathFinder(Arrays.asList(source, target), 10);
    }
}
