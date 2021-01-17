package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private LineRepository lineRepository;
    private StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        List<Line> allLines = lineRepository.findAll();
        Station source = stationService.findById(sourceStationId);
        Station target = stationService.findById(targetStationId);
        return PathFinder.findPath(allLines, source, target)
                        .map(path -> new PathResponse(
                            PathStationResponse.newList(path.getStations()),
                            path.getDistance())
                        )
                        .orElseThrow(() -> new IllegalArgumentException("최단 경로를 찾을 수 없습니다."));
    }
}
