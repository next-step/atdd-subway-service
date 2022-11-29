package nextstep.subway.path.application;

import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        validateStation(source);
        validateStation(target);
        List<Line> lines = lineRepository.findAll();
        Path path = pathFinder.findPath(lines, source, target);
        return PathResponse.from(path);
    }

    private void validateStation(Long stationId){
        stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("station id와 일치하는 역을 찾을 수 없습니다."));
    }
}
