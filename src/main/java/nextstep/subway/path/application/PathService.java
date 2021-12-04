package nextstep.subway.path.application;

import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse getShortCut(Long sourceStationId, Long targetStationId) {
        Station source = stationService.findStationById(sourceStationId);
        Station target = stationService.findStationById(targetStationId);
        Set<Line> lines = new HashSet<>(lineService.findLineByStation(source));
        lines.addAll(lineService.findLineByStation(target));
        GraphPath<Station, DefaultWeightedEdge> shortCut = PathFinder.findShortCut(lines, source, target);
        if (shortCut == null) {
            throw new ServiceException("최단 경로를 찾을 수 없습니다");
        }
        return PathResponse.of(shortCut);
    }
}
