package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(long sourceId, long targetId) {
        Station sourceStation = stationService.findById(sourceId);
        Station targetStation = stationService.findById(targetId);
        validSameStation(sourceStation, targetStation);
        PathFinder pathFinder = new PathFinder();
        return PathResponse.from(pathFinder.findShortestPath(lineService.findPathBag(), sourceStation, targetStation));
    }

    private void validSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 서로 다른역이어야 합니다");
        }
    }
}
