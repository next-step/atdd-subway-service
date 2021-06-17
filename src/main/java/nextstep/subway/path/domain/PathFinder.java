package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {

    private final Station source;
    private final Station target;
    private final List<Line> lines;

    public PathFinder(Station source, Station target, List<Line> lines) {

        if (source.equals(target)) {
            throw new NotFoundPathException("출발역과 도착역이 같습니다.");
        }

        this.source = source;
        this.target = target;
        this.lines = lines;
    }

    public ShortestPath findShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            for (Section section : line.getSections()) {

                Station upStation = section.getUpStation();
                Station downStation = section.getDownStation();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                graph.setEdgeWeight(graph.addEdge(upStation, downStation),
                                    section.getDistance().getValue());
            }
        }

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
            List<Station> shortestPath = path.getVertexList();
            int distance = (int) path.getWeight();

            return new ShortestPath(shortestPath, distance);
        } catch (IllegalArgumentException e) {
            throw new NotFoundPathException("경로가 연결되어 있지 않습니다.");
        }
    }
}
