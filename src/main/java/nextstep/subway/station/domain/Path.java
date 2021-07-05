package nextstep.subway.station.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Path {

    private static final int BASIC_FARE = 1_250;

    private GraphPath graphPath;

    public Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> stations() {
        return graphPath.getVertexList();
    }

    public int distance() {
        return (int)graphPath.getWeight();
    }

    public int fare() {
        return BASIC_FARE + extraFare() + DistanceBasedFarePolicy.overFare(distance());
    }

    public int fare(int age) {
        int fare = fare();
        return fare - AgeDiscountPolicy.discountFare(age, fare);
    }

    private int extraFare() {
        List<Integer> extraFares = sections().stream()
                                            .map(Section::getLine)
                                            .map(Line::getExtraFare)
                                            .collect(Collectors.toList());
        return Collections.max(extraFares);
    }

    private List<Section> sections() {
        return graphPath.getEdgeList();
    }
}
