package nextstep.subway.path.application;

import nextstep.subway.map.application.MapService;
import nextstep.subway.map.domain.Map;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final MapService mapService;

    public PathService(StationService stationService, MapService mapService) {
        this.stationService = stationService;
        this.mapService = mapService;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station source = stationService.findStationById(pathRequest.getSource());
        Station target = stationService.findStationById(pathRequest.getTarget());

        Map map = mapService.getMap();
        return map.findShortestPath(source, target);
    }
}
