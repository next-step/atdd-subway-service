package nextstep.subway.path.domain;

import static java.util.Collections.*;
import static java.util.Comparator.*;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class Path {

    private final GraphPath<Station, SectionEdge> value;

    Path(GraphPath<Station, SectionEdge> value) {
        this.value = value;
    }

    public static Path of(GraphPath<Station, SectionEdge> path) {
        return new Path(path);
    }

    public double distance() {
        return value.getWeight();
    }

    public List<Station> getStations() {
        return unmodifiableList(value.getVertexList());
    }

    /*
    TODO 객체지향 생활체조 적용 필요
     */
    public int totalFareOf(Member member) {
        return applyDiscount(member.getAge());
    }

    private int getFare() {
        return 1250 + calculateOverFare(distance()) + getMaxLineFare();
    }

    private int getMaxLineFare() {
        return value.getEdgeList().stream()
            .map(SectionEdge::getLineFare)
            .max(naturalOrder())
            .orElse(0);
    }

    private int calculateOverFare(double distance) {
        if (distance <= 10) {
            return 0;
        }

        if (distance <= 50) {
            return (int) ((Math.ceil((distance - 10) / 5)) * 100);
        }

        return 100 * 8 + (int) ((Math.ceil((distance - 50) / 8)) * 100);
    }

    private int applyDiscount(int age) {
        if (age >= 20) {
            return getFare();
        }

        if (13 <= age && age < 19) {
            return (int) ((getFare() - 350) * 0.8);
        }

        if (6 <= age && age < 13) {
            return (int) ((getFare() - 350) * 0.5);
        }

        return 0;
    }
}
