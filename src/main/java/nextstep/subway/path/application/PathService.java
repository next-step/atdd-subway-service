package nextstep.subway.path.application;

import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;


    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId, Integer age) {
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);

        ensureNotSameStation(sourceStation, targetStation);

        List<Line> lines = lineService.findAll();
        PathFinder pathFinder = new DijkstraPathFinder(lines);
        Path shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);
        Fare fare = new Fare(shortestPath, age);
        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), fare.getCalculatedFare());
    }

    private void ensureNotSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(SubwayExceptionMessage.SAME_SOURCE_TARGET.getMessage());
        }
    }
}
