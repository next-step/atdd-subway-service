package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.sections.domain.Section;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {

    public PathResponse findShortestPath(List<Section> allSection, Station sourceStation, Station targetStation) {
        return null;
    }
}
