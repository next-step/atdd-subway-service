package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JgraphtPathFinder implements PathFinderInterface {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public JgraphtPathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        lines.forEach(line -> {
            addVertexes(line.getStations());
            setEdgeWeights(line.getSections());
        });
    }

    private void addVertexes(List<Station> stations) {
        stations.forEach(graph::addVertex);
    }

    private void setEdgeWeights(Sections sections) {
        sections.getSections().forEach(section -> {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
            graph.setEdgeWeight(edge, section.getDistance());
        });
    }

    @Override
    public boolean containsStation(Station station) {
        return graph.containsVertex(station);
    }

    @Override
    public boolean stationsConnected(Station src, Station dest) {
        return !Objects.isNull(dijkstraShortestPath.getPath(src, dest));
    }

    @Override
    public Path getShortestPath(Station src, Station dest) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = dijkstraShortestPath.getPath(src, dest);
        return new Path(graphPath.getVertexList(), graphPath.getWeight());
    }

}
