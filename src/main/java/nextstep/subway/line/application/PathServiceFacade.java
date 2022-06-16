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
public class PathServiceFacade {

    private final StationService stationService;
    private final LineService lineService;

    public PathServiceFacade(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(long srcStationId, long dstStationId) {
        Station srcStation = stationService.findById(srcStationId);
        Station dstStation = stationService.findById(dstStationId);
        List<Line> lines = lineService.findAllLines();

        PathFinder pathFinder = new PathFinder(lines);

        GraphPath path = pathFinder.findPath(srcStation, dstStation);

        return PathResponse.of(path.getVertexList(), path.getWeight());
    }
}
