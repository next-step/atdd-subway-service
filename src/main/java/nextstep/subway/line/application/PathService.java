package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathGraph;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PathService {
    private StationService stationService;
    private LineRepository lineRepository;
    private PathGraph pathGraph;

    public PathService(StationService stationService, LineRepository lineRepository, PathGraph pathGraph) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.pathGraph = pathGraph;
    }

    public PathResponse path(Long sourceId, Long targetId) {
        GraphPath path = pathGraph.findPath(sourceId, targetId, lineRepository.findAll());

        List<String> vertexList = path.getVertexList();
        List<StationResponse> collect = vertexList.stream().map(
                vertex -> StationResponse.of(stationService.findStationById(Long.valueOf(vertex)))
        ).collect(Collectors.toList());

        return new PathResponse(collect, (int)path.getWeight());
    }
}
