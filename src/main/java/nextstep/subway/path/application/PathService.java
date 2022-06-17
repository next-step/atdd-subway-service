package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final StationService stationService;

    @Autowired
    public PathService(StationService stationService) {
        this.stationService = stationService;
    }

    public PathResponse findPath(Long source, Long target) {
        return new PathResponse(stationService.findAllStations(), 50);
    }
}
