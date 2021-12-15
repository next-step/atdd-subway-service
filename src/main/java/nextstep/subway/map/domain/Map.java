package nextstep.subway.map.domain;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.BadRequestApiException;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.apache.commons.lang3.ObjectUtils;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class Map {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private Map(List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    public static Map of(List<Section> sections) {
        return new Map(sections);
    }

    public PathResponse findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        if (ObjectUtils.isEmpty(path)) {
            throw new BadRequestApiException(ErrorCode.UNCOUPLED_PATH);
        }

        Stations stations = Stations.of(path.getVertexList());
        int distance = (int) path.getWeight();
        return PathResponse.of(stations.toResponse(), distance);
    }
}
