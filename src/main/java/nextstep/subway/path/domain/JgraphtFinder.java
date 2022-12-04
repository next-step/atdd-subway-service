package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class JgraphtFinder implements Finder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public JgraphtFinder() {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    @Override
    public void isExistPath(Station source, Station target) throws IllegalArgumentException {
        dijkstraShortestPath.getPath(source, target);
    }

    @Override
    public List<Station> getVertexList(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    @Override
    public int getWeight(Station source, Station target) {
        return (int) dijkstraShortestPath.getPath(source, target).getWeight();
    }

    @Override
    public void setEdgeWeight(Section section, int weight) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), weight);
    }

    @Override
    public void addVertex(Station station) {
        graph.addVertex(station);
    }
}
