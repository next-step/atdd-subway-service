package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class PathFinder {
    private static final int EMPTY = 0;
    private static final String ERR_TEXT_INVALID_LINE_DATA = "지하철 노선 데이터가 올바르지 않습니다.";
    private static final String ERR_TEXT_TWO_STATIONS_ARE_SAME = "출발역과 도착역은 서로 다른 역이어야 합니다.";
    private static final String ERR_TEXT_CAN_NOT_MOVE_THIS_PATH = "출발역에서 도착역으로 이동할 수 없습니다.";
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graphByDistance;

    private PathFinder(final List<Section> sections) {
        this.graphByDistance = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        sections.forEach(section -> section.addToPath(addToPathGraph()));
    }

    private Consumer<Section> addToPathGraph() {
        return section -> {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();
            graphByDistance.addVertex(upStation);
            graphByDistance.addVertex(downStation);
            graphByDistance.setEdgeWeight(graphByDistance.addEdge(upStation, downStation), section.getDistance());
        };
    }

    public static PathFinder of(final List<Section> sections) {
        if (sections.size() <= EMPTY) {
            throw new IllegalArgumentException(ERR_TEXT_INVALID_LINE_DATA);
        }

        return new PathFinder(sections);
    }

    public GraphPath<Station, DefaultWeightedEdge> findShortestPath(final Station departure, final Station arrival) {
        makeSureThatTheTwoStationsAreDifference(departure.getId(), arrival.getId());

        return Optional.ofNullable(findShortestPathByDijkstra(departure, arrival))
            .orElseThrow(() -> new IllegalArgumentException(ERR_TEXT_CAN_NOT_MOVE_THIS_PATH));
    }

    private void makeSureThatTheTwoStationsAreDifference(final Long departureStationId, final Long arrivalStationId) {
        if (Objects.equals(departureStationId, arrivalStationId)) {
            throw new IllegalArgumentException(ERR_TEXT_TWO_STATIONS_ARE_SAME);
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findShortestPathByDijkstra(final Station departure, final Station arrival) {
        return new DijkstraShortestPath<>(graphByDistance).getPath(departure, arrival);
    }
}
