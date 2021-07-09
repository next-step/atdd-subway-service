package nextstep.subway.path.dto;

import nextstep.subway.exception.NoPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class StationGraphPath {
    GraphPath<Station, DefaultWeightedEdge> stationGraphPath;

    private StationGraphPath(GraphPath<Station, DefaultWeightedEdge> stationGraphPath) {
        this.stationGraphPath = stationGraphPath;
    }

    public StationGraphPath(Station source, Station target, List<Line> lines) {
        this.stationGraphPath = generateShortestPath(source, target, lines);
    }

    public GraphPath<Station, DefaultWeightedEdge> generateShortestPath(Station source, Station target, List<Line> lines) {
        WeightedMultigraph graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setUpGraphByLines(graph, lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> stationPath = dijkstraShortestPath.getPath(source, target);
        checkExistPath(stationPath);
        return stationPath;
    }

    private void setUpGraphByLines(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().flatMap(line -> line.getSections().values().stream())
                .forEach(
                        section -> {
                            graph.addVertex(section.getUpStation());
                            graph.addVertex(section.getDownStation());
                            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                        }
                );
    }

    private void checkExistPath(GraphPath<Station, DefaultWeightedEdge> stationPath) {
        if (stationPath == null) {
            throw new NoPathException("연결된 경로가 없습니다.");
        }
    }

    public List<Station> getPathStations() {
        return stationGraphPath.getVertexList();
    }

    public int getDistance() {
        return (int) stationGraphPath.getWeight();
    }
}
