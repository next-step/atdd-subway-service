package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    public static PathFinder from(Lines lines) {
        return new PathFinder(initGraph(lines));
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(Lines lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        List<Station> stations = lines.getStations();
        stations.forEach(graph::addVertex);

        List<Section> sections = lines.getSections();
        sections.forEach(section ->
                graph.setEdgeWeight(
                        graph.addEdge(
                                section.getUpStation(),
                                section.getDownStation()),
                        section.getDistance()
                ));
        return graph;
    }

    public Path findShortestPath(Station sourceStation, Station targetStation) {
        validateDuplicateStation(sourceStation, targetStation);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);
        return Path.of(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void validateDuplicateStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new BadRequestException("출발역과 도착역이 같은 경우 최단 거리를 구할 수 없습니다.");
        }
    }
}
