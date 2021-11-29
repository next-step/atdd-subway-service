package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    public PathResult findShortestPath(Sections sections, Station source, Station target) {

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        sections.getStations().forEach(station -> graph.addVertex(station));
        sections.getSections().forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));

        DijkstraShortestPath dijkstra = new DijkstraShortestPath(graph);
        GraphPath path = dijkstra.getPath(source, target);
        List<Station> shortestPath = path.getVertexList();
        int distance = (int) path.getWeight();

        return new PathResult(shortestPath, distance);
    }
}
