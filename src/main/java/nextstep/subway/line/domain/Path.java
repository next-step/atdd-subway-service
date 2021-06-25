package nextstep.subway.line.domain;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class Path {
    public static final String SAME_STATION = "같은 역입니다.";

    private final DijkstraShortestPath<Station, Station> dijkstraShortestPath;

    public Path(DijkstraShortestPath<Station, Station> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static Path of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addPath(line.sections(), graph);
        }

        return new Path(new DijkstraShortestPath(graph));
    }

    private static void addPath(List<Section> sections, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Section section : sections) {

            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.distanceToInteger());
        }
    }

    public PathResponse findShortestPath(Station source, Station target) {
        if (source.equals(target)) {
            throw new RuntimeException(SAME_STATION);
        }
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new RuntimeException(Sections.NOT_FOUND_SECTION);
        }

        return PathResponse.of(shortestPath.stream().map(Station::toResponse).collect(Collectors.toList()), findPathWeight(source, target));
    }

    private int findPathWeight(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }
}
