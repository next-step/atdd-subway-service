package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private Integer fare;

    public Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = FareCalculator.calculateFare(distance);
    }

    public void calculateFare(Member member, List<Line> lines) {
        Integer totalFare = fare + lines.stream()
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
        fare = DiscountCalculator.getFare(member, totalFare);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
