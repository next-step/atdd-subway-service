package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;

    }

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);

        PathFinder.addLines(lineService.findLines());

        return PathResponse.from(PathFinder.findShortestPath(sourceStation, targetStation));
    }

}
