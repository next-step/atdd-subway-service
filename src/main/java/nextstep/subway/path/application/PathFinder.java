package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathFinder {
    public PathResponse findShortestPath(List<Section> sections, Station source, Station target) {
        return new PathResponse();
    }
}
