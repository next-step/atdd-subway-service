package nextstep.subway.path.application;

import java.util.Map;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        Map<Long, Station> stationMap = stationService.findMapByIds(source, target);
        Station sourceStation = stationMap.get(source);
        Station targetStation = stationMap.get(target);
        PathFinder pathFinder = new PathFinder(new Lines(lineRepository.findAll()));
        return pathFinder.findPath(sourceStation, targetStation);
    }

}
