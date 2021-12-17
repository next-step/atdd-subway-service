package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DijkstraShortestPathCalculator implements ShortestPathCalculator {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public DijkstraShortestPathCalculator() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    @Override
    public Optional<GraphPath> calculatePath(List<Section> sections, Station sourceStation, Station targetStation) {
        addVertexAndEdge(sections, graph);
        createDijkstraShortestPath();
        return Optional.ofNullable(dijkstraShortestPath.getPath(sourceStation, targetStation));
    }

    private void createDijkstraShortestPath() {
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addVertexAndEdge(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = findStations(sections);

        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private List<Station> findStations(List<Section> sections) {
        return sections.stream()
                .flatMap(Section::stations)
                .distinct()
                .collect(Collectors.toList());
    }
}
