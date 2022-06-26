package nextstep.subway.navigation.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.application.FareCalculatorService;
import nextstep.subway.fare.domain.DiscountPolicy;
import nextstep.subway.fare.domain.DiscountType;
import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.common.Messages.NOT_CONNECTED_SOURCE_TARGET_STATION;

public class Navigation {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private GraphPath<Station, SectionEdge> path;

    public static Navigation of(List<Line> persistLines) {
        return new Navigation(persistLines);
    }

    private Navigation(List<Line> persistLines) {
        persistLines.forEach(this::settingGraph);
    }

    private void settingGraph(Line line) {
        line.getSections().getSections().forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);

            SectionEdge sectionEdge = graph.addEdge(upStation, downStation);
            sectionEdge.addSection(section);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        });
    }

    public NavigationResponse findShortest(Station sourceStation, Station targetStation, LoginMember loginMember) {
        path = new DijkstraShortestPath<>(graph).getPath(sourceStation, targetStation);
        checkFindShortestPath(path);

        List<Station> vertexList = path.getVertexList();
        int distance = (int) path.getWeight();
        int fare = FareCalculatorService.calculate(distance) + maxLineExtraFare(path);

        DiscountPolicy discountPolicy = DiscountType.ofDiscountPolicy(loginMember.getAge());
        int discountedFare = discountPolicy.discountFare(fare);

        return NavigationResponse.of(vertexList, distance, discountedFare);
    }

    private void checkFindShortestPath(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(NOT_CONNECTED_SOURCE_TARGET_STATION);
        }
    }

    private int maxLineExtraFare(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList().stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
