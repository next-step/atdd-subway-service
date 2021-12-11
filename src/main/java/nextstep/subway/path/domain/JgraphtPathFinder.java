package nextstep.subway.path.domain;

import nextstep.subway.error.SubwayInternalException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class JgraphtPathFinder implements PathFinder {

    public Path findPath(List<Line> lines, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = initGraph(lines);
        validationSameStation(sourceStation, targetStation);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

        validationNotConnect(graphPath);

        List<Station> stationsPath = graphPath.getVertexList();
        Long distance = (long) dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();

        return new Path(stationsPath, distance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addVertex(graph, line);
            setEdgeWeight(graph, line);
        }
        return graph;
    }

    private void setEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSections().stream().forEach(
                section -> {
                    graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                }
        );
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        line.getSortedStations().stream().forEach(
                station -> graph.addVertex(station)
        );
    }

    private void validationNotConnect(GraphPath graphPath) {
        if(Objects.isNull(graphPath)) {
            throw new SubwayInternalException("출발역과 도착역이 이어져 있지 않습니다.");
        }
    }

    private void validationSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayInternalException("출발역과 도착역이 같습니다.");
        }
    }
}
