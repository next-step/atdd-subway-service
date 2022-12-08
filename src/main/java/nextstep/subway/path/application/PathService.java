package nextstep.subway.path.application;

import nextstep.subway.domain.FareCalculator;
import nextstep.subway.line.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.domain.PathFinder;
import nextstep.subway.path.vo.Path;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }


    public PathResponse findShortestPath(Long source, Long target, int age) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        Sections sections = findAllSections();

        PathFinder pathFinder = PathFinder.from(sections.getSections());
        Path path = pathFinder.findAllStationsByStations(sourceStation, targetStation);

        int fare = findFare(sections, path.getStations(), path.getDistance(), age);

        return PathResponse.of(path.getStations(), path.getDistance());
    }

    private Sections findAllSections() {
        return Sections.from(
                lineRepository.findAll()
                        .stream()
                        .flatMap(line -> line.getSections().stream())
                        .collect(Collectors.toList())
        );
    }

    private int findFare(Sections sections, List<Station> stations, int distance, int age) {
        Lines lines = sections.findLinesFrom(stations);

        return 0;
    }

    private Station findStationById(Long stationId) {
        return stationService.findStationById(stationId);
    }
}
