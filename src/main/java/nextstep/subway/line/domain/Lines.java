package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Lines {
    List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public DijkstraShortestPath createPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            registerPath(line, graph);
        }

        return new DijkstraShortestPath(graph);
    }

    private void registerPath(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        addVertices(line, graph);
        addEdges(line, graph);
    }

    private void addVertices(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.findStations()
                .forEach(graph::addVertex);
    }

    private void addEdges(Line line, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        line.getSections()
                .getSections()
                .forEach(section -> addEdgeWith(addEdge(section, graph), section.getDistance(), graph));
    }

    private DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        return graph.addEdge(section.getUpStation(), section.getDownStation());
    }

    private void addEdgeWith(DefaultWeightedEdge edge, int weight, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        graph.setEdgeWeight(edge, weight);
    }
}
