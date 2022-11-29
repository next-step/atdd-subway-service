package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        lines.forEach(line -> line.addPath(this));
    }

    public PathResponse find(Station sourceStation, Station targetStation) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        List<Station> shortestPathStations = shortestPath.getPath(sourceStation, targetStation).getVertexList();
        Distance distance = new Distance(shortestPath.getPathWeight(sourceStation, targetStation));

        return new PathResponse(shortestPathStations, distance);
    }

    public void addPath(Station upStation, Station downStation, Distance distance) {
        graph.addVertex(upStation);
        graph.addVertex(downStation);
        graph.setEdgeWeight(graph.addEdge(upStation, downStation), distance.toDouble());
    }
}
