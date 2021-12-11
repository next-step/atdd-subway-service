package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JGraphTPathFinder implements PathFinder {
    @Override
    public PathResponse findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        String source = sourceStation.getName();
        String target = targetStation.getName();
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        lines.stream()
                .flatMap(line -> line.getStations()
                        .stream())
                .forEach(station -> graph.addVertex(station.getName()));
        lines.stream()
                .flatMap(line -> line.getSections()
                        .getSections()
                        .stream())
                .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation()
                        .getName(), section.getDownStation()
                        .getName()), section.getDistance()));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        List<StationResponse> stations = lines.stream()
                .flatMap(line -> line.getStations()
                        .stream()).distinct().filter(station -> shortestPath.contains(station.getName()))
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());


        int pathWeight = (int) dijkstraShortestPath.getPathWeight(source, target);

        return new PathResponse(stations, pathWeight);
    }

    public JGraphTPathFinder() {
    }
}
