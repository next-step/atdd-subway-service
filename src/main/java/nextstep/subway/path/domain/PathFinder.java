package nextstep.subway.path.domain;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class PathFinder {

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public Path findFastPaths(List<Line> lines, Station sourceStation, Station targetStation) {
        setGraphVertexAndEdge(lines);
        GraphPath<Station, DefaultWeightedEdge> graphPath = getGraphPath(sourceStation, targetStation);
        checkIsLinkedStation(graphPath);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight());
    }

    private void setGraphVertexAndEdge(List<Line> lines) {
        lines.forEach(line -> {
            addVertex(line);
            setEdgeWeight(line);
        });
    }

    private void addVertex(Line line) {
            line.getStations().forEach(graph::addVertex);
    }

    private void setEdgeWeight(Line line) {
        line.getSections().getList().forEach(section -> {
            graph.setEdgeWeight(graph.addEdge(
                    section.getUpStation(),
                    section.getDownStation()),
                    section.getDistance()
            );
        });
    }

    private GraphPath<Station, DefaultWeightedEdge> getGraphPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(sourceStation, targetStation);
    }

    private void checkIsLinkedStation(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new InvalidRequestException("출발역과 도착역이 연결 되어있지 않습니다.");
        }
    }

}
