package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.farePolicy.MemberDiscountPolicyService;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class Path {
    public static final String SAME_STATION = "같은 역입니다.";

    private final DijkstraShortestPath<Station, Station> dijkstraShortestPath;
    private final Fare lineExtraFare;

    public Path(DijkstraShortestPath<Station, Station> dijkstraShortestPath, Fare lineExtraFare) {
        this.dijkstraShortestPath = dijkstraShortestPath;
        this.lineExtraFare = lineExtraFare;
    }

    public static Path of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        Fare mostExtraFare = new Fare();

        for (Line line : lines) {
            addPath(line.sections(), graph);
            mostExtraFare = mostExtraFare.gt(line.extraFare());
        }

        return new Path(new DijkstraShortestPath(graph), mostExtraFare.sum(new Fare(Fare.DEFAULT_USE_FARE_AMOUNT)));
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
            throw new IllegalArgumentException(SAME_STATION);
        }
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new IllegalArgumentException(Sections.NOT_FOUND_SECTION);
        }

        int distance = findPathDistance(source, target);
        Fare totalFare = this.lineExtraFare.calculateTotalFare(distance);

        return PathResponse.of(shortestPath.stream().map(Station::toResponse).collect(Collectors.toList()), findPathDistance(source, target), totalFare.amount());
    }

    private int findPathDistance(Station source, Station target) {
        return (int) dijkstraShortestPath.getPathWeight(source, target);
    }

    public PathResponse findShortestPath(Station source, Station target, MemberDiscountPolicyService memberDiscountPolicyService) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SAME_STATION);
        }
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new IllegalArgumentException(Sections.NOT_FOUND_SECTION);
        }

        int distance = findPathDistance(source, target);
        Fare fare = lineExtraFare.calculateTotalFare(distance);
        Fare totalFare = memberDiscountPolicyService.discount(fare);

        return PathResponse.of(shortestPath.stream().map(Station::toResponse).collect(Collectors.toList()), findPathDistance(source, target), totalFare.amount());
    }
}
