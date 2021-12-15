package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.IllegalPathException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JGraphTPathFinder implements PathFinder {
    public JGraphTPathFinder() {
    }

    @Override
    public PathResponse findPath(List<Line> lines, Long sourceId, Long targetId) {
        validateSameSourceTarget(sourceId, targetId);
        DijkstraShortestPath dijkstraShortestPath = getDijkstraShortestPath(lines);
        List<StationResponse> stations = getShortestPathStationResponses(lines, sourceId, targetId, dijkstraShortestPath);
        int pathWeight = (int) dijkstraShortestPath.getPathWeight(String.valueOf(sourceId), String.valueOf(targetId));
        return new PathResponse(stations, pathWeight);
    }

    private List<StationResponse> getShortestPathStationResponses(List<Line> lines, Long sourceId, Long targetId, DijkstraShortestPath dijkstraShortestPath) {
        List<String> shortestPath = getShortestPath(String.valueOf(sourceId), String.valueOf(targetId), dijkstraShortestPath);
        List<StationResponse> stations = getPathStationResponses(lines, shortestPath);
        return stations;
    }

    private DijkstraShortestPath getDijkstraShortestPath(List<Line> lines) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        makeGraph(lines, graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath;
    }

    private List<String> getShortestPath(String source, String target, DijkstraShortestPath dijkstraShortestPath) {
        List<String> shortestPath;
        try {
            shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (RuntimeException e) {
            throw new IllegalPathException("경로를 찾을 수 없습니다");
        }
        return shortestPath;
    }

    private void validateSameSourceTarget(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalPathException("출발역과 도착역이 같습니다");
        }
    }

    private List<StationResponse> getPathStationResponses(List<Line> lines, List<String> shortestPath) {
        List<Station> stations = lines.stream()
                .flatMap(line -> line.getStations().stream()).distinct().collect(Collectors.toList());
        return shortestPath.stream().map(stationId -> stations.stream()
                        .filter(station -> String.valueOf(station.getId())
                                .equals(stationId)).findFirst())
                .map(station -> StationResponse.of(station.get()))
                .collect(Collectors.toList());
    }

    private void makeGraph(List<Line> lines, WeightedMultigraph<String, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(line -> line.getStations()
                        .stream())
                .forEach(station -> graph.addVertex(String.valueOf(station.getId())));
        lines.stream()
                .flatMap(line -> line.getSections()
                        .getSections()
                        .stream())
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(String.valueOf(section.getUpStation()
                        .getId()), String.valueOf(section.getDownStation()
                        .getId())), section.getDistance()));
    }
}
