package nextstep.subway.path.apllication;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
    private StationService stationService;
    private LineService lineService;
    private Path path;

    public PathService(StationService stationService, LineService lineService, Path path) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.path = path;
    }

    public PathResponse findPath(Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Line> lines = lineService.findAll();
        path.initLines(lines);
        return getPathResponse(sourceStation, targetStation);
    }

    private PathResponse getPathResponse(Station sourceStation, Station targetStation) {
        GraphPath<Long, DefaultWeightedEdge> pathFind = path.find(sourceStation, targetStation);
        List<StationResponse> stationResponses = stationService.findAllStations(pathFind.getVertexList());
        stationResponses = stationResponseOrderByVertexList(pathFind, stationResponses);

        return new PathResponse(stationResponses, pathFind.getWeight());
    }

    private List<StationResponse> stationResponseOrderByVertexList(GraphPath<Long, DefaultWeightedEdge> pathFind, List<StationResponse> stationResponses) {
        List<StationResponse> stationResponsesOrderBy = new ArrayList<>();
        pathFind.getVertexList().stream().forEach(s -> System.out.println(s));
        pathFind.getVertexList().stream().forEach(
                id -> stationResponsesOrderBy.add(findStationById(stationResponses, id))
        );
        return stationResponsesOrderBy;
    }

    private StationResponse findStationById(List<StationResponse> stationResponses, Long id) {
        return stationResponses.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
