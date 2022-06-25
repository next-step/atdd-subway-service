package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.ShortestPathResponse;
import nextstep.subway.path.dto.StationsResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathFinder {
    public ShortestPathResponse findShortestPath(List<Line> allLines, Station starting, Station destination) {
        // TODO: Validation 추가
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = this.createStationGraph(allLines);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(stationGraph);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(starting, destination);

        List<Station> stations = path.getVertexList();
        double distance = shortestPath.getPathWeight(starting, destination);

        return new ShortestPathResponse(StationsResponse.of(new Stations(stations)), distance);
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> createStationGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.forEach(
                line -> line.getSections()
                .toList()
                .forEach(section -> {
                    stationGraph.addVertex(section.getUpStation());
                    stationGraph.addVertex(section.getDownStation());
                    stationGraph.setEdgeWeight(stationGraph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().toInt());
                })
        );

        return stationGraph;
    }
}
