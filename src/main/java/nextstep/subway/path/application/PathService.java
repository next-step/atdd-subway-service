package nextstep.subway.path.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.fare.domain.SubwayFareCalculator;
import nextstep.subway.line.domain.LineFare;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
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
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target, int age) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        Sections sections = findAllSections();

        PathFinder pathFinder = DijkstraShortestPathFinder.from(sections.getSections());
        List<Station> stations = pathFinder.findAllStationsInShortestPath(sourceStation, targetStation);
        int distance = pathFinder.findShortestDistance(sourceStation, targetStation);

        int fare = findFare(sections, stations, distance, age);

        return PathResponse.of(stations, distance, fare);
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
        LineFare lineFare = lines.findMaxLineFare();

        FareCalculator fareCalculator = SubwayFareCalculator.of(lineFare.getFare(), age);
        return fareCalculator.calculate(distance);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.STATION_NOT_EXIST));
    }
}
