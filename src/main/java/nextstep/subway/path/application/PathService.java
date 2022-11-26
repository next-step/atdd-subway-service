package nextstep.subway.path.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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

    public PathResponse findShortestPath(Long source, Long target) {
        Station sourceStation = stationRepository.findById(source).orElseThrow(EntityNotFoundException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(EntityNotFoundException::new);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(lines);
        Path shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);
        return PathResponse.from(shortestPath);
    }
}
