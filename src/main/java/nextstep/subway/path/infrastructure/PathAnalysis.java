package nextstep.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.station.domain.Station;

public class PathAnalysis {
    private static PathAnalysis pathAnalysis;

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath ;

    private PathAnalysis() {
    }

    public static PathAnalysis getInstance() {
        if (pathAnalysis == null) {
            pathAnalysis = new PathAnalysis();

            return pathAnalysis;
        }

        return pathAnalysis;
    }
    
    public void initialze(Sections sections) {
        this.graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.shortestPath = new DijkstraShortestPath<>(this.graph);

        for (Section section : sections.getSections()) {
            addPath(section);
        }
    }

    public void addPath(Section section) {
        this.graph.addVertex(section.getUpStation());
        this.graph.addVertex(section.getDownStation());

        DefaultWeightedEdge newEdge = this.graph.addEdge(section.getDownStation(), section.getUpStation());
        int distance = section.getDistance().value();
        this.graph.setEdgeWeight(newEdge, distance);
    }

    public void removePath(Section section) {
        this.graph.removeEdge(section.getDownStation(), section.getUpStation());
    }

    public ShortestPathInfo findShortestPaths(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> graphPath = this.shortestPath.getPath(source, target);

        return ShortestPathInfo.of(graphPath.getVertexList(), Distance.of((int)graphPath.getWeight()));
    }
}
