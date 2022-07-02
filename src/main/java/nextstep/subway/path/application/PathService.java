package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinderStrategy;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinderStrategy pathFinderStrategy;

    public PathService(LineRepository lineRepository, StationService stationService, PathFinderStrategy pathFinderStrategy) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinderStrategy = pathFinderStrategy;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestDistance(PathRequest request) {
        List<Line> lines = lineRepository.findAll();
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        Path path = pathFinderStrategy.getShortestDistance(new Lines(lines), source, target);

        return PathResponse.of(path);
    }
}
