package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;

    public PathService(StationService stationService) {
        this.stationService = stationService;
    }

    public PathResponse findPath(PathRequest request) {
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        throw new NotImplementedException();
    }

}
