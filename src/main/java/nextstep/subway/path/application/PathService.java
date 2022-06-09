package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long departureStationId, Long arrivalStationId) {
        List<Line> lines = lineService.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Station departureStation = stationService.findById(departureStationId);
        Station arrivalStation = stationService.findById(arrivalStationId);

        List<Station> vertexList = pathFinder.findVertexList(departureStation, arrivalStation);
        int weight = pathFinder.getWeight(departureStation, arrivalStation);
        return PathResponse.of(vertexList, weight);
    }
}
