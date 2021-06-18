package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        this.graph = initializeStationGraph(lines);
    }

    public ShortestPath findShortestPath(Station source, Station target) {

        verifySourceNotEqualTarget(source, target);

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);

            return new ShortestPath(path.getVertexList(), (int) path.getWeight());
        } catch (IllegalArgumentException e) {
            throw new NotFoundPathException("경로가 연결되어 있지 않습니다.");
        }
    }

    private void verifySourceNotEqualTarget(Station source, Station target) {
        if (source.equals(target)) {
            throw new NotFoundPathException("출발역과 도착역이 같습니다.");
        }
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initializeStationGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultiGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.stream()
             .flatMap(line -> line.getSections().stream())
             .forEach(section -> {
                 Station upStation = section.getUpStation();
                 Station downStation = section.getDownStation();

                 weightedMultiGraph.addVertex(upStation);
                 weightedMultiGraph.addVertex(downStation);

                 weightedMultiGraph.setEdgeWeight(weightedMultiGraph.addEdge(upStation, downStation),
                                     section.getDistance().getValue());
             });

        return weightedMultiGraph;
    }
}
