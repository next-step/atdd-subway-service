package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PathService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceStationId, Long targetStationId) {

        List<Line> lines = lineRepository.findAll();

        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(() -> new RuntimeException("출발역이 존재하지 않습니다."));
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(() -> new RuntimeException("도착역이 존재하지 않습니다."));

        PathFinder pathFinder = PathFinder.of(lines);
        Path path = pathFinder.findShortestPath(sourceStation, targetStation);
        return PathResponse.of(path);
    }
}
