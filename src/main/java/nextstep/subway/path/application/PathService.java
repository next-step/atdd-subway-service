package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.graphPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(final Long startStationId, final Long destinationStationId) {
        final Station start = stationService.findById(startStationId);
        final Station destination = stationService.findById(destinationStationId);
        final List<Line> lines = lineService.findAll();
        final graphPathFinder pathFinder = new graphPathFinder();

        return PathResponse.of(pathFinder.getShortestPath(lines, start, destination));
    }
}
