package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Path {
    private List<Station> stations;
    private int distance;
    private Set<Line> lines;

    public Path(List<Station> stations, int distance, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.distance = distance;
        this.lines = toLines(sectionEdges);
    }

    private Set<Line> toLines(List<SectionEdge> edges) {
        return edges.stream()
            .map(SectionEdge::getLine)
            .collect(Collectors.toSet());
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getCalculateFare(Integer age) {
        int fare = DistanceFarePolicy.calculate(distance);
        fare += applyAdditionalFare();
        if (Objects.nonNull(age)) {
            AgeFarePolicy ageDiscountPolicy = AgeFarePolicy.of(age);
            fare = ageDiscountPolicy.discount(fare);
        }
        return fare;
    }

    private int applyAdditionalFare() {
        return lines.stream()
            .map(Line::getSurcharge)
            .max(Integer::compareTo)
            .orElse(0);
    }
}
