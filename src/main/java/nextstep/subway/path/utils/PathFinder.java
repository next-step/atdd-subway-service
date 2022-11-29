package nextstep.subway.path.utils;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    public Path findPath(List<Line> lines, Long source, Long target) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stream()
            .flatMap(line -> line.getStations().stream())
            .forEach(station -> graph.addVertex(String.valueOf(station.getId())));

        lines.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(String.valueOf(section.getUpStation().getId()),
                                      String.valueOf(section.getDownStation().getId())),
                        section.getDistance().getDistance()));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(String.valueOf(source), String.valueOf(target)).getVertexList();

        List<Station> stations = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList());

        List<StationResponse> stationResponses = shortestPath.stream()
                .map(stationId -> stations.stream().filter(station -> String.valueOf(station.getId()).equals(stationId)).findFirst())
                .map(station -> StationResponse.of(station.get()))
                .collect(Collectors.toList());

        int pathWeight = (int) dijkstraShortestPath.getPathWeight(String.valueOf(source), String.valueOf(target));

        return new Path(stationResponses, pathWeight);
    }
}
