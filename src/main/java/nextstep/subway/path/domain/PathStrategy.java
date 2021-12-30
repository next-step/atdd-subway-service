package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PathStrategy {
    Path getShortestPath(final List<Line> lines, final Station source, final Station target);
}