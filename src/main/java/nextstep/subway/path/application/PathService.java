package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;

    public PathService(StationService stationService) {
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {

        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        return null;
    }
}
