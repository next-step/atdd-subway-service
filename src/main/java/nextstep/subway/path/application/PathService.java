package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public PathService(
            LineRepository lineRepository,
            StationRepository stationRepository,
            StationService stationService
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        validateStation(source);
        validateStation(target);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.findPath(source, target);
        List<StationResponse> stations = stationService.findAllByIdIsIn(path.getStationIds());
        return PathResponse.from(stations, path.getDistance());
    }

    private void validateStation(Long stationId){
        stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("station id와 일치하는 역을 찾을 수 없습니다."));
    }
}
