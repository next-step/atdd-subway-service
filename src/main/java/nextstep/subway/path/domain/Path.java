package nextstep.subway.path.domain;

import java.util.Comparator;
import java.util.List;
import nextstep.subway.fare.domain.DiscountFarePolicy;
import nextstep.subway.fare.domain.DistanceFarePolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;

public class Path {
    private final List<Long> stationIds;
    private final List<SectionEdge> sectionEdges;
    private final int distance;

    public Path(List<Long> stationIds, int distance, List<SectionEdge> sectionEdges) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.sectionEdges = sectionEdges;
    }

    public static Path of(List<Long> stationIds, int distance, List<SectionEdge> sectionEdges) {
        return new Path(stationIds, distance, sectionEdges);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public Fare calculate(Integer age) {
        Fare fare = DistanceFarePolicy.calculate(this.distance);
        if(age != null) {
            fare = DiscountFarePolicy.calculate(fare, age);
        }
        return fare.plus(getMaxAddedFare());
    }

    public Fare getMaxAddedFare() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .map(Line::getExtraFare)
                .max(Comparator.comparing(Fare::value))
                .orElseThrow(() -> new IllegalArgumentException("추가요금을 찾을 수 없습니다"));
    }

}