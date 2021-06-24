package nextstep.subway.path.domain;

import nextstep.subway.line.domain.SectionEdge;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ShortestPath {

    public static final int BASIC_FARE = 1250;
    public static final int BASIC_DISTANCE = 10;

    private GraphPath path;
    private User user;

    public ShortestPath(GraphPath path) {
        validate(path);

        this.path = path;
    }

    private void validate(GraphPath path) {
        if (path == null) {
            throw new RuntimeException("최단경로가 Null 입니다.");
        }
    }

    private int maxAdditionalFareIn(GraphPath<Station, SectionEdge> path) {
        List<SectionEdge> sectionEdges = path.getEdgeList();
        sectionEdges.stream()
                .forEach(sectionEdge -> System.out.println(sectionEdge.getAdditionalFare()));

        return sectionEdges.stream()
                .max(Comparator.comparing(sectionEdge -> sectionEdge.getAdditionalFare()))
                .orElseThrow(NoSuchElementException::new)
                .getAdditionalFare();
    }

    private int calculateOverFare(int distance) {
        if (distance > 0) {
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return 0;
    }

    public List<Station> stations() {
        return path.getVertexList();
    }

    public int totalDistance() {
        return (int) path.getWeight();
    }

    public int fare() {
        if (user == null) {
            return totalFare();
        }

        return discountTotalFare();
    }

    private int discountTotalFare() {
        return (int) user.discount(totalFare());
    }

    private int totalFare() {
        return BASIC_FARE
                + calculateOverFare(totalDistance() - BASIC_DISTANCE)
                + maxAdditionalFareIn(path);
    }

    public ShortestPath withUser(Member member) {
        user = User.typeOf(member);
        return this;
    }

    public ShortestPath withoutUser() {
        user = User.NONE;
        return this;
    }
}
