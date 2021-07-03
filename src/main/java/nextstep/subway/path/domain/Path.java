package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nextstep.subway.path.additionalfarepolicy.AdditionalFarePolicy;
import nextstep.subway.path.additionalfarepolicy.AdditionalFareCalculator;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import nextstep.subway.error.CustomException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.error.ErrorMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.additionalfarepolicy.memberfarepolicy.MemberDiscountPolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public class Path {
    private Path() {
    }

    public static PathResponse findShortestPath(List<Line> lines, Station source, Station target, LoginMember loginMember) {
        DijkstraShortestPath<Station, Station> dijkstraShortestPath = init(lines);
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        if (shortestPath.isEmpty()) {
            throw new CustomException(ErrorMessage.NOT_FOUND_SECTION);
        }

        Fare lineFare = setupLineFare(lines, shortestPath);
        Distance distance = new Distance((int) dijkstraShortestPath.getPathWeight(source, target));

        MemberDiscountPolicy policy = MemberDiscountPolicy.getPolicy(loginMember);
        AdditionalFarePolicy additionalFarePolicy = new AdditionalFareCalculator();

        Fare resultFare = additionalFarePolicy.calculate(lineFare, distance, policy);

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

    private static Fare setupLineFare(List<Line> lines, List<Station> stations) {
        Fare mostExtraFare = new Fare();
        Set<Line> stationContainLines = new HashSet<>();

        for (int i = 0; i < stations.size() - 1; i++) {
            stationContainLines.add(lineContainUpAndDownStation(lines, stations.get(i), stations.get(i + 1)));
        }

        for (Line line : stationContainLines) {
            mostExtraFare = line.extraFare().gt(mostExtraFare);
        }
        return mostExtraFare;
    }

    private static Line lineContainUpAndDownStation(List<Line> lines, Station upStation, Station downStation) {
        return lines.stream()
                .filter(line -> line.isContainStation(upStation) && line.isContainStation(downStation))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_FOUND_SECTION));
    }
}
