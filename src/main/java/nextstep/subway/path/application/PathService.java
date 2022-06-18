package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public List<StationResponse> findShortestPath(Long sourceId, Long targetId) {
        return new ArrayList<>();
    }
}