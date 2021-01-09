package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Lines lines = new Lines(lineService.findLines());
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        Path path = new PathFinder(lines).findShortestPath(source, target);
        return PathResponse.of(path);
    }
}
