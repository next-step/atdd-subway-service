package nextstep.subway.path.application;

import nextstep.subway.fare.domain.AgePolicy;
import nextstep.subway.fare.domain.DistanceFare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private static final String NONE_EXISTS_STATION = "역이 존재하지 않습니다";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId, int age) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        ShortestPath shortestPath = pathFinder.getShortestPath(sourceStation, targetStation);
        Line line = findLine(Lines.of(lines), shortestPath.getStations());
        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance(), findFare(line, shortestPath.getDistance(), age));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NONE_EXISTS_STATION));
    }

    private Line findLine(Lines lines, List<Station> stations) {
        return lines.findMaxFareLineByStations(stations);
    }

    private int findFare(Line line, int distance, int age) {
        FareCalculator fareCalculator = new FareCalculator(line, new DistanceFare(distance), AgePolicy.valueOfAge(age));
        return fareCalculator.getCalculcate();
    }

}
