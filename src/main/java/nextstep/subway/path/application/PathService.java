package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.exception.ErrorEnum;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId, int age) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();
        Sections sections = findAllSections();

        PathFinder pathFinder = PathFinder.from(lines);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);

        int fare = findFare(sections, path, age);

        return PathResponse.from(path, fare);
    }

    private int findFare(Sections sections, Path path, int age) {
        return FareCalculator.distanceWithAgeCalculate(path.getDistance(), age);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException(ErrorEnum.NOT_EXISTS_STATION.message()));
    }

    private Sections findAllSections() {
        return Sections.from(
                lineRepository.findAll()
                        .stream()
                        .flatMap(line -> line.getSections().stream())
                        .collect(Collectors.toList())
        );
    }
}
