package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public final class ShortestPathFinder {

    private final DijkstraShortestPath<Station, DefaultEdge> shortestPath;

    private ShortestPathFinder(Lines lines) {
        validate(lines);
        shortestPath = dijkstraShortestPath(lines);
    }

    public static ShortestPathFinder from(Lines lines) {
        return new ShortestPathFinder(lines);
    }

    public Path path(Station source, Station target) {
        GraphPath<Station, DefaultEdge> path = shortestPath.getPath(source, target);
        return Path.of(Stations.from(path.getVertexList()), Distance.from(path.getWeight()));
    }

    private DijkstraShortestPath<Station, DefaultEdge> dijkstraShortestPath(Lines lines) {
        return new DijkstraShortestPath<>(weightedStationGraph(lines));
    }

    private WeightedMultigraph<Station, DefaultEdge> weightedStationGraph(Lines lines) {
        WeightedMultigraph<Station, DefaultEdge> graph =
            new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : lines.stationList()) {
            graph.addVertex(station);
        }
        for (Section section : lines.sectionList()) {
            graph.setEdgeWeight(
                graph.addEdge(section.upStation(), section.downStation()), section.distanceValue()
            );
        }
        return graph;
    }

    private void validate(Lines lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("최단 경로를 조회할 노선들이 비어있을 수 없습니다.");
        }
    }
}
