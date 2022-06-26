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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PathFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathFinder.class);

    public static ShortestPathResponse findShortestPath(List<Line> allLines, Station starting, Station destination) {
        validateSameStation(starting, destination);

        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = createStationGraph(allLines);
        validateIsExistStation(stationGraph, starting, destination);

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(stationGraph);
        GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(starting, destination);
        validateConnectedStation(path);

        List<Station> stations = path.getVertexList();
        double distance = shortestPath.getPathWeight(starting, destination);

        return new ShortestPathResponse(StationsResponse.of(new Stations(stations)), (int) distance);
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> createStationGraph(List<Line> lines) {
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

    private static void validateIsExistStation(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph, Station starting, Station destination) {
        if (!stationGraph.containsVertex(starting) || !stationGraph.containsVertex(destination)) {
            LOGGER.error(ErrorMessage.NOT_FOUND_STATION);
            throw new NotFoundException(ErrorMessage.NOT_FOUND_STATION);
        }
    }

    private static void validateSameStation(Station starting, Station destination) {
        if (starting.isSame(destination)) {
            LOGGER.error(ErrorMessage.SAME_STATION);
            throw new BadRequestException(ErrorMessage.SAME_STATION);
        }
    }

    private static void validateConnectedStation(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            LOGGER.error(ErrorMessage.NOT_CONNECTED_STATION);
            throw new BadRequestException(ErrorMessage.NOT_CONNECTED_STATION);
        }
    }
}
