package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.station.domain.Station;

public class PathFinder {
    private LineDijkstraShortestPath graph;

    public PathFinder(Lines lines) {
        graph = new LineDijkstraShortestPath(lines);
    }

    public ShortestPathResponse findShortestPath(Station source, Station target) {
        return new ShortestPathResponse(
                this.graph.getShortestStationPath(source, target),
                this.graph.getShortestPathStationDistance(source, target)
        );
    }
}
