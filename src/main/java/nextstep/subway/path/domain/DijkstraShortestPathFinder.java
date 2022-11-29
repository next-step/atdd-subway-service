package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public DijkstraShortestPathFinder(List<Line> lines) {
        lines.stream().forEach(line -> {
            addVertex(line);
            setEdgeWeight(line.getSections());
        });
    }

    private void addVertex(Line line) {
        line.getStations()
                .forEach(graph::addVertex);
    }

    private void setEdgeWeight(List<Section> sections) {
        sections.stream()
                .forEach(section -> graph.setEdgeWeight(addEdge(section), section.getDistance()));
    }

    private DefaultWeightedEdge addEdge(Section section) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    public Path find(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(source, target);

        List<Station> stations = graphPath.getVertexList();
        double weight = graphPath.getWeight();
        return Path.of(stations, (int) weight);
    }
}
