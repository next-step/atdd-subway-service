package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private StationService stationService;
    private LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPaths(Long sourceId, Long targetId) {
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);
        List<LineResponse> lineResponses = lineService.findLines();
        PathFinder pathFinder = PathFinder.create(lineResponses);
        PathResponse pathResponse = pathFinder.toResponse(sourceStation, targetStation);
        return pathResponse;
    }
}
