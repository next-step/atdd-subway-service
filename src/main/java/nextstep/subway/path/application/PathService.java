package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;

    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());

        PathFinder pathFinder = new PathFinder(lines);
        return pathFinder.getPath(sourceStation, targetStation);
    }
}
