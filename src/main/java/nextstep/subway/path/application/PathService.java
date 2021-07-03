package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private static final String FIND_SAME_STATION_ERROR = "두 역은 같은 역일 수 없습니다.";
    private static final String NOT_CONNECTED_STATIONS_ERROR = "두 역이 연결되어 있지 않습니다.";

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        validateFindStations(sourceId, targetId);

        List<Line> lines = lineService.fineAllLines();
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);
        return findPathByDijkstraShortestPath(lines, sourceStation, targetStation);
    }

    private PathResponse findPathByDijkstraShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);
        subwayGraph.addAllVertex(lines);
        subwayGraph.addAllEdge(lines);

        GraphPath<Station, SectionEdge> path = new DijkstraShortestPath(subwayGraph).getPath(sourceStation, targetStation);
        validateConnect(path);
        return new PathResponse(new SubwayPath(path));
    }

    private void validateFindStations(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException(FIND_SAME_STATION_ERROR);
        }
    }

    private void validateConnect(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(NOT_CONNECTED_STATIONS_ERROR);
        }
    }

}
