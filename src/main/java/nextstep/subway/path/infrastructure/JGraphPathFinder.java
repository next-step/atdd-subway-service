package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.exception.EdgeCreateException;
import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * packageName : nextstep.subway.path.application
 * fileName : PathFinder
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@Component
public class JGraphPathFinder implements PathFinder {

    @Override
    public Path getShortestPath(List<Line> lines, List<Station> stations, Long srcStationId, Long destStationId) {
        checkStationIds(srcStationId, destStationId);

        Station srcStation = getStation(stations, srcStationId);
        Station destStation = getStation(stations, destStationId);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        drawGraph(lines, stations, graph);

        try {
            GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(srcStation, destStation);

            return Path.of(srcStation, destStation, path.getVertexList(), Distance.of((int) path.getWeight()));
        } catch (NullPointerException npe) {
            throw new PathNotFoundException();
        }
    }

    private void drawGraph(List<Line> lines, List<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        drawVertex(stations, graph);

        checkVertexDrawn(graph);

        drawEdge(lines, graph);
    }

    private void checkVertexDrawn(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        if (graph.vertexSet().isEmpty()) {
            throw new EdgeCreateException();
        }
    }

    private void drawEdge(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        lines.forEach(line -> line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().intValue())
                )
        );
    }

    private void drawVertex(List<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.stream().forEach(graph::addVertex);
    }

    private Station getStation(List<Station> stations, Long srcStationId) {
        return stations.stream()
                .filter(it -> it.getId().equals(srcStationId))
                .findFirst().
                orElseThrow(StationNotFoundException::new);
    }

    private void checkStationIds(Long srcStationId, Long destStationId) {
        if (Objects.equals(srcStationId, destStationId)) {
            throw new PathBeginIsEndException(srcStationId, destStationId);
        }
    }
}
