package nextstep.subway.path.application;

import nextstep.subway.common.exception.path.PathBeginIsEndException;
import nextstep.subway.common.exception.path.PathNotFoundException;
import nextstep.subway.common.exception.station.StationNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
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
public class PathFinder {
    public PathResponse getShortestPath(List<Line> lines, List<Station> stations, Long srcStationId, Long destStationId) {
        List<Station> result;
        if (Objects.equals(srcStationId, destStationId)) {
            throw new PathBeginIsEndException();
        }

        try {
            Station srcStation = stations.stream()
                    .filter(it -> it.getId().equals(srcStationId))
                    .findFirst()
                    .orElseThrow(StationNotFoundException::new);

            Station destStation = stations.stream()
                    .filter(it -> it.getId().equals(destStationId))
                    .findFirst()
                    .orElseThrow(StationNotFoundException::new);

            WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

            stations.stream().forEach(graph::addVertex);

            lines.stream()
                    .forEach(line -> line.getSections().stream()
                            .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().intValue())));

            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

            result = dijkstraShortestPath.getPath(srcStation, destStation).getVertexList();
        } catch (NullPointerException npe) {
            throw new PathNotFoundException();
        }

        return PathResponse.of(result);
    }
}
