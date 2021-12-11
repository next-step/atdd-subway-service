package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JGraphTPathFinder implements PathFinder {
    @Override
    public PathResponse findPath(List<Line> lines, Long sourceId, Long targetId) {
        String source = String.valueOf(sourceId);
        String target = String.valueOf(targetId);
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        makeGraph(lines, graph);
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        List<StationResponse> stations = getPathStationResponses(lines, shortestPath);
        int pathWeight = (int) dijkstraShortestPath.getPathWeight(source, target);
        return new PathResponse(stations, pathWeight);
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

    public JGraphTPathFinder() {
    }
}
