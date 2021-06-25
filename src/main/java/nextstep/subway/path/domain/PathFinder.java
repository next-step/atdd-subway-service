package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {

    void makeGraph(List<Line> lines);

    void addVerticesAndEdgesOf(Line line);

    ShortestPath executeDijkstra(Station sourceStation, Station targetStation);

    List<ShortestPath> executeKShortest(Station sourceStation, Station targetStation);

    void validateSourceTarget(Station source, Station target);
}
