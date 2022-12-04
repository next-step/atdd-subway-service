package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.policy.AgeDiscountPolicy;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

import static nextstep.subway.exception.ErrorMessage.NOT_CONNECT_START_ARRIVE_STATION;
import static nextstep.subway.exception.ErrorMessage.NOT_SEARCH_SAME_START_ARRIVE_STATION;

public class PathFinder {

    private final WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private final DijkstraShortestPath dijkstraShortestPath;

    public PathFinder(List<Line> lines) {
        for (Line line : lines) {
            addStationVertex(line.stations());
            addEdgeSection(line.getSections());
        }
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }

    private void addStationVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeSection(List<Section> sections) {
        for (Section section : sections) {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance().value());
        }
    }

    public Path getShortestPath(AuthMember authMember, Station sourceStation, Station targetStation) {
        validCheck(sourceStation, targetStation);
        GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(sourceStation, targetStation);

        // TODO: 요금 계산을 모두 해서 보내는게 맞는지 고민 필요
        int distance = (int) graphPath.getWeight();
        int extraFare = maxedLineExtraFare(graphPath.getEdgeList()) + DistanceFare.calculateDistanceFare(distance);
        AgeDiscountPolicy discountPolicy = AgeFare.getDiscountPolicy(authMember);
        int ageDiscountFare = discountPolicy.discount(extraFare);

        return new Path(graphPath.getVertexList(), distance, ageDiscountFare);
    }

    private void validCheck(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(NOT_SEARCH_SAME_START_ARRIVE_STATION.getMessage());
        }
        if (!graph.containsVertex(sourceStation) || !graph.containsVertex(targetStation)) {
            throw new IllegalArgumentException(NOT_CONNECT_START_ARRIVE_STATION.getMessage());
        }
    }

    private int maxedLineExtraFare(List<SectionEdge> edgeList) {
        return edgeList.stream()
                .mapToInt(SectionEdge::getLineExtraFare)
                .max()
                .orElse(0);
    }


}
