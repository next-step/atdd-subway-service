package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.dto.PathResponse;
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
    final private StationRepository stationRepository;
    final private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("출발역이 존재하지 않습니다."));
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("도착역이 존재하지 않습니다."));

        if (sourceStation.equals(targetStation)) {
            throw new RuntimeException("출발역과 도착역이 같습니다.");
        }

        PathResult result = Path.of(lines).findShortest(sourceStation, targetStation);

        return PathResponse.of(
                result.getStations().stream()
                        .map(station -> StationResponse.of(station))
                        .collect(Collectors.toList()),
                result.getDistance());
    }
}
