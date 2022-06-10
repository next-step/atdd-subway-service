package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {
    final private StationService stationService;
    final private LineRepository lineRepository;

    public PathService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new RuntimeException("출발역과 도착역이 같습니다.");
        }

        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);

        PathResult result = Path.of(lines).findShortest(sourceStation, targetStation);

        return PathResponse.of(
                result.getStations().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toList()),
                result.getDistance());
    }
}
