package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    final StationService stationService;
    final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findBestPath(Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        List<Line> lines = findAllLines();

        Path path = new Path(lines, source, target);
        return new PathResponse(path.getBestPath(), path.getBestPathDistance());
    }

    private Station findStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private List<Line> findAllLines() {
        return lineService.findAllLines();
    }
}