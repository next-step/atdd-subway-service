package nextstep.subway.path.application;

import nextstep.subway.common.exception.path.PathBeginIsEndException;
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
        if(Objects.equals(srcStationId, destStationId)) {
            throw new PathBeginIsEndException();
        }

        Station srcStation = stations.stream()
                .filter(it -> it.getId().equals(srcStationId))
                .findFirst().get();
                //.orElseThrow(StationNotFoundException::new);
        Station destStation = stations.stream()
                .filter(it -> it.getId().equals(destStationId))
                .findFirst().get();
                //.orElseThrow(StationNotFoundException::new);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        stations.stream().forEach(graph::addVertex);

        lines.stream()
                .forEach(line -> line.getSections().stream()
                        .forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().intValue())));

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(srcStation, destStation).getVertexList();

        return PathResponse.of(shortestPath);
    }
}
