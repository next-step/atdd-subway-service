package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathEdge;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.exception.EdgeCreateException;
import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
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

        WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph<>(PathEdge.class);
        DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        drawGraph(lines, stations, graph);

        try {
            GraphPath<Station, PathEdge> path = dijkstraShortestPath.getPath(srcStation, destStation);
            return Path.of(path.getEdgeList(), path.getVertexList(), srcStation, destStation, Distance.of((int) path.getWeight()));
        } catch (NullPointerException npe) {
            throw new PathNotFoundException();
        }
    }

    private void drawGraph(List<Line> lines, List<Station> stations, WeightedMultigraph<Station, PathEdge> graph) {
        drawVertex(stations, graph);
        checkVertexDrawn(graph);
        lines.forEach(line -> drawEdge(line, graph));
    }

    private void checkVertexDrawn(WeightedMultigraph<Station, PathEdge> graph) {
        if (graph.vertexSet().isEmpty()) {
            throw new EdgeCreateException();
        }
    }

    private void drawEdge(Line line, WeightedMultigraph<Station, PathEdge> graph) {
        line.getSections().forEach(section -> {
            PathEdge pathEdge = PathEdge.of(section);
            graph.addEdge(pathEdge.sourceVertex(), pathEdge.targetVertex(), pathEdge);
            graph.setEdgeWeight(pathEdge, pathEdge.weight());
        });
    }

    private void drawVertex(List<Station> stations, WeightedMultigraph<Station, PathEdge> graph) {
        stations.forEach(graph::addVertex);
    }

    private Station getStation(List<Station> stations, Long srcStationId) {
        return stations.stream()
                .filter(it -> it.getId().equals(srcStationId))
                .findFirst().
                orElseThrow(StationNotFoundException::new);
    }

    private void checkStationIds(Long srcStationId, Long destStationId) {
        if (Objects.equals(srcStationId, destStationId)) {
            throw new PathBeginIsEndException();
        }
    }
}
