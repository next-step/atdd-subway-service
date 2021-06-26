package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;

public class PathService {

    private final LineRepository lines;
    private final StationRepository stations;

    public PathService(LineRepository lines, StationRepository stations) {
        this.lines = lines;
        this.stations = stations;
    }

    public PathResponse getShortestPath(Long source, Long target) {
        return null;
    }
}
