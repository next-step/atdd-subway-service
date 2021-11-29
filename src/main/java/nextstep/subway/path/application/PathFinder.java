package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;

public class PathFinder {

    public PathResult findShortestPath(Sections sections, Station source, Station target) {
        DijkstraGraph dijkstraGraph = new DijkstraGraph();
        DijkstraPath path = dijkstraGraph.getPath(sections);
        return path.find(source, target);
    }
}
