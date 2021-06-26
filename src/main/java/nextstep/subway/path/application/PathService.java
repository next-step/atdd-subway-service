package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        validateSameStations(source, target);
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        return PathResponse.of(PathFinder.of(lines)
                .findPath(sourceStation, targetStation));
    }

    private void validateSameStations(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalStateException("경로조회 출발역과 도착역이 같습니다.");
        }
    }
}
