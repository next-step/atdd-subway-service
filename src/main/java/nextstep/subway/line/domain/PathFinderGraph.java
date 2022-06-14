package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public interface PathFinderGraph {
    void addVertices(List<Station> stations);

    void addEdges(List<Section> sections);

    void addEdgeWith(DefaultWeightedEdge edge, int weight, WeightedMultigraph<Station, DefaultWeightedEdge> graph);

    DefaultWeightedEdge addEdge(Section section, WeightedMultigraph<Station, DefaultWeightedEdge> graph);

    DijkstraShortestPath getPath();
}
