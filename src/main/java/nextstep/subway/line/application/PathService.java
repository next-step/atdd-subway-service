package nextstep.subway.line.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }


    public PathResponse findPath(long sourceStationId, long destinationStationId) {
        Station sourceStation = stationService.findById(sourceStationId);
        Station destStation = stationService.findById(destinationStationId);
        List<Line> lines = lineService.findAllLines();

        PathFinder pathFinder = new PathFinder(lines);

        GraphPath path = pathFinder.findPath(sourceStation, destStation);

        return PathResponse.of(path.getVertexList(), path.getWeight());
    }
}
