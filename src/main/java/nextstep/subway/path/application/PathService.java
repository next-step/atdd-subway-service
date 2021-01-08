package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;
    private PathFinder pathFinder;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPathByIds(Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();
        this.pathFinder = PathFinder.initialPathFinder(lines);;

        Station sourceStation = stationRepository.findById(sourceId).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(targetId).orElseThrow(RuntimeException::new);
        return pathFinder.getShortestPath(sourceStation, targetStation);
    }
}
