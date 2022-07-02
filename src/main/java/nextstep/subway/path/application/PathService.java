package nextstep.subway.path.application;

import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
        return calculateFare(shortestPath, age);
    }

    private PathResponse calculateFare(Path shortestPath, Integer age) {
        int distance = shortestPath.getDistance();
        final int BASIC_FARE = 1250;
        int fare = BASIC_FARE;
        if (distance > 50) {
            int extraDistance = distance - 50;
            fare += (Math.ceil((double) extraDistance / 8) * 100);
            distance -= extraDistance;
        }

        if (distance <= 50 && distance >= 10) {
            int extraDistance = distance - 10;
            fare += (Math.ceil((double) extraDistance / 5) * 100);
        }

        int lineSurcharge = shortestPath.getSectionEdges().stream()
                .max(Comparator.comparing(SectionEdge::getLineSurcharge))
                .orElseThrow(NoSuchElementException::new)
                .getLineSurcharge();

        fare += lineSurcharge;

        if (age >= 13 && age < 19) {
            fare -= ((fare - 350) * 0.2);
        }

        if (age >= 6 && age < 13) {
            fare -= ((fare - 350) * 0.5);
        }

        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), fare);
    }

    private void ensureNotSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(SubwayExceptionMessage.SAME_SOURCE_TARGET.getMessage());
        }
    }
}
