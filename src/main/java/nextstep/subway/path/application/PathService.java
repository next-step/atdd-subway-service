package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.AgeFarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DijkstraPathFinder;
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

    public PathResponse findShortestPath(AgeFarePolicy ageFarePolicy, Long source, Long target) {
        Path shortestPath = findShortestPathInGraph(source, target);
        shortestPath.convertFareByAgeFarePolicy(ageFarePolicy);
        return PathResponse.from(shortestPath);
    }

    private Path findShortestPathInGraph(Long source, Long target) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = DijkstraPathFinder.createGraph(lines);
        return pathFinder.findShortestPath(sourceStation, targetStation);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_역.getErrorMessage()));
    }
}
