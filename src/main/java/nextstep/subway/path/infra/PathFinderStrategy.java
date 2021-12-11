package nextstep.subway.path.infra;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinderStrategy {
    List<Station> findStations(List<Line> lines, Station source, Station target);
    Distance findDistance(List<Line> lines, Station source, Station target);
}
