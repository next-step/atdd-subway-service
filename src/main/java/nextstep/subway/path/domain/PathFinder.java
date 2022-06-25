package nextstep.subway.path.domain;

import nextstep.subway.constant.ErrorMessage;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundException;
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
        this.validateSameStation(starting, destination);

        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = this.createStationGraph(allLines);
        this.validateIsExistStation(stationGraph, starting, destination);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(stationGraph);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(starting, destination);
        this.validateConnectedStation(path);

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

    private void validateIsExistStation(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph, Station starting, Station destination) {
        if (!stationGraph.containsVertex(starting) || !stationGraph.containsVertex(destination)) {
            System.out.println(ErrorMessage.NOT_FOUND_STATION);
            throw new NotFoundException(ErrorMessage.NOT_FOUND_STATION);
        }
    }

    private void validateSameStation(Station starting, Station destination) {
        if (starting.isSame(destination)) {
            System.out.println(ErrorMessage.SAME_STATION);
            throw new BadRequestException(ErrorMessage.SAME_STATION);
        }
    }

    private void validateConnectedStation(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            System.out.println(ErrorMessage.NOT_CONNECTED_STATION);
            throw new BadRequestException(ErrorMessage.NOT_CONNECTED_STATION);
        }
    }
}
