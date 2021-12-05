package nextstep.subway.path.application;

import nextstep.subway.path.exception.PathBeginIsEndException;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * packageName : nextstep.subway.path.application
 * fileName : PathFinder
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@Component
public class PathFinder {
    public PathResponse getShortestPath(List<Line> lineList, List<Station> stationList, Long srcStationId, Long destStationId) {
        List<Station> result;

        if (Objects.equals(srcStationId, destStationId)) {
            throw new PathBeginIsEndException(srcStationId, destStationId);
        }

        Stations stations = Stations.of(stationList);
        Lines lines = Lines.of(lineList);
        Station srcStation = stations.getStation(srcStationId);
        Station destStation = stations.getStation(destStationId);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        stations.setVertex(graph);
        lines.setEdge(graph);

        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            result = dijkstraShortestPath.getPath(srcStation, destStation).getVertexList();
        } catch(NullPointerException npe) {
            throw new PathNotFoundException();
        }

        return PathResponse.of(result);
    }
}
