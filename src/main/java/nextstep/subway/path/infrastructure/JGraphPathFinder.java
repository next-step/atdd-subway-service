package nextstep.subway.path.infrastructure;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.EdgeCreateException;
import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exception.StationNotFoundException;
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
    public PathResponse getShortestPath(List<Line> lines, List<Station> stations, Long srcStationId, Long destStationId) {
        checkStationIds(srcStationId, destStationId);

        List<Station> result;
        Station srcStation = getStation(stations, srcStationId);
        Station destStation = getStation(stations, destStationId);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        drawGraph(lines, stations, graph);

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            result = dijkstraShortestPath.getPath(srcStation, destStation).getVertexList();
        } catch (NullPointerException npe) {
            throw new PathNotFoundException();
        }

        return PathResponse.of(result);
    }

    private void drawGraph(List<Line> lines, List<Station> stations, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.stream().forEach(graph::addVertex);

        if (graph.vertexSet().isEmpty()) {
            throw new EdgeCreateException();
        }

        lines.forEach(line -> line.getSections()
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().intValue())
                )
        );
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
