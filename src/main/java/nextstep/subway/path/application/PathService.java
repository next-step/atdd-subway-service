package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SectionWeightedEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long startStationId, Long destinationStationId) {
        List<Line> lines = lineService.findAllLines();

        Station source = stationService.findStationById(startStationId);
        Station target = stationService.findStationById(destinationStationId);

        PathFinder pathFinder = PathFinder.of(lines);

        GraphPath<Station, SectionWeightedEdge> shortestPath = pathFinder.findShortestPath(source, target);
        return PathResponse.of(shortestPath);
    }
}
