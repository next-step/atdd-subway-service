package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathFinderStrategy {

    Path shortestPath(Station source, Station target);

    List<Line> lineOnPath(List<Line>lines);

}
