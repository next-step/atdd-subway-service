package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface ShortestPathCalculator {
    Optional<GraphPath> calculatePath(List<Section> sections, Station sourceStation, Station targetStation);
}
