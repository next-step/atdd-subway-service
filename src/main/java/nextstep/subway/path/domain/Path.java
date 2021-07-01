package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.error.CustomException;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.error.ErrorMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.memberfarepolicy.MemberDiscountPolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class Path {
    private Path() {}

    public static PathResponse findShortestPath(List<Line> lines, Station source, Station target, LoginMember loginMember) {
        DijkstraShortestPath<Station, Station> dijkstraShortestPath = init(lines);
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new CustomException(ErrorMessage.NOT_FOUND_SECTION);
        }

        MemberDiscountPolicy policy = MemberDiscountPolicy.getPolicy(loginMember);
        Distance distance = new Distance((int) dijkstraShortestPath.getPathWeight(source, target));

        Fare lineExtraFare = setupLineOverFare(lines, shortestPath);
        Fare fare = lineExtraFare.calculateTotalFare(distance);
        Fare resultFare = policy.applyDiscount(fare);

        return PathResponse.of(shortestPath.stream().map(Station::toResponse).collect(Collectors.toList()), distance, resultFare);
    }

    private static DijkstraShortestPath<Station, Station> init(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Line line : lines) {
            addPath(line.sections(), graph);
        }
         return new DijkstraShortestPath(graph);
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

    private static Fare setupLineOverFare(List<Line> lines, List<Station> stations) {
        Fare mostExtraFare = new Fare();
        Set<Line> stationContainLines = new HashSet<>();
        for (Station station : stations) {
            stationContainLines.addAll(lines.stream().filter(line -> line.isContainStation(station))
                    .collect(Collectors.toSet()));
        }

        for (Line line : stationContainLines) {
            mostExtraFare = line.extraFare().gt(mostExtraFare);
        }
        return mostExtraFare;
    }
}
