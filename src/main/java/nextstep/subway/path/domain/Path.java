package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import java.math.BigDecimal;
import java.util.List;

public class Path {

    private static final int BASE_FARE = 1250;
    private final List<Station> stations;
    private final Distance distance;
    private Fare fare;

    private Path(List<Station> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(List<Station> stations, int distance, List<Section> sections) {
        Fare maxExtraFare = findMaxExtraFare(sections);
        Fare distanceExtraFare = calculatorExtraFareBy(distance);
        return new Path(
                stations,
                Distance.from(distance),
                Fare.from(BASE_FARE)
                        .plus(maxExtraFare)
                        .plus(distanceExtraFare)
        );
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }

    public void calculateAgeDiscount(LoginMember loginMember) {
        if (loginMember.isGuest()) {
            return;
        }

        this.fare = AgeDiscountFarePolicy.valueOf(loginMember.getAge(), fare);
    }

    private static Fare findMaxExtraFare(List<Section> shortestSections) {
        return shortestSections.stream()
                .map(section -> section.getLine()
                        .getExtraFare()
                        .value())
                .max(BigDecimal::compareTo)
                .map(Fare::from)
                .orElse(Fare.from(BigDecimal.ZERO));
    }

    private static Fare calculatorExtraFareBy(int distance) {
        return DistanceFarePolicy.valueOf(distance);
    }
}
