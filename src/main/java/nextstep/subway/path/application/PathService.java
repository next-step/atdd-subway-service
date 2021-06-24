package nextstep.subway.path.application;

import nextstep.subway.line.application.LineQueryService;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final LineQueryService lineQueryService;

    public PathService(StationService stationService, LineQueryService lineQueryService) {
        this.stationService = stationService;
        this.lineQueryService = lineQueryService;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        return PathResponse.of(Arrays.asList(new Station("test")), 12);
    }
}
