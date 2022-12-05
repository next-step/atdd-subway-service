package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
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

    public PathResponse findShortestPath(Long sourceId, Long targetId,int age) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        List<Station> stations = pathFinder.getShortestPath(sourceStation, targetStation);
        int distance = pathFinder.getShortestDistance(sourceStation, targetStation);
        int lineFare = findFare(Lines.of(lines), stations, distance, age);
        return new PathResponse(stations, distance);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NONE_EXISTS_STATION));
    }

    private int findFare(Lines lines, List<Station> stations, int distance, int age) {
        Lines filteredLines = lines.getLinesFrom(stations);
        int fare = filteredLines.getMaxLineFare();
        return fare;
    }

}
