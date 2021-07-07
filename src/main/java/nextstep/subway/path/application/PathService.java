package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest pathRequest) {
        ShortestPath shortestPath = findShortestPath(pathRequest);
        return PathResponse.of(shortestPath.toStationResponse(),
            shortestPath.getDistance(),
            shortestPath.calculateFareWithPolicy());
    }

    private ShortestPath findShortestPath(PathRequest pathRequest) {
        Station sourceStation = stationService.findById(pathRequest.getSource());
        Station targetStation = stationService.findById(pathRequest.getTarget());
        List<Section> allSections = lineRepository.findAll().stream()
            .flatMap(line -> line.getSections().stream())
            .collect(toList());

        PathFinder pathFinder = PathFinder.of(allSections);
        return pathFinder.findShortestPath(sourceStation, targetStation);
    }
}
