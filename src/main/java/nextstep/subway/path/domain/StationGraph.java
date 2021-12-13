package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public interface StationGraph {

    StationGraph createGraph(List<Line> lines);

    boolean containsStation(Station station);

    Path getShortestPath(Station source, Station target);
}
