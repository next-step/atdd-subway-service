package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinder {
    List<Station> findStations(List<Line> lines, Station source, Station target);
    Distance findDistance(List<Line> lines, Station source, Station target);
    ShortestPath findShortestPath(List<Line> lines, Station source, Station target);
}
