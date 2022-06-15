package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public interface PathFinderGraph {
    void addVertices(List<Station> stations);

    void addEdges(List<Section> sections);

    void addEdgeWith(int weight, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge edge);

    void addEdge(Section section, WeightedMultigraph<Station, SectionGraphEdge> graph, SectionGraphEdge sectionGraphEdge);

    DijkstraShortestPath getPath();
}
