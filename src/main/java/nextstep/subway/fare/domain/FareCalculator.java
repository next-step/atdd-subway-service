package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.exception.CannotCalculateAdditionalFareException;
import nextstep.subway.station.domain.Stations;

import static nextstep.subway.fare.domain.Fare.DEFAULT_FARE;

public class FareCalculator {

    private FareCalculator() {
    }

    public static Fare calculate(Sections sections, Age age, Stations stations, Distance distance) {
        Fare baseFare = DEFAULT_FARE.add(calculateAdditionalFare(sections, stations));
        baseFare = FaresByDistance.calculate(baseFare, distance);
        baseFare = DiscountByAge.calculate(baseFare, age);
        return baseFare;
    }

    private static Fare calculateAdditionalFare(Sections sections, Stations stations) {
        return stations.get().stream()
                .map(sections::findMaxFareByStation)
                .max(Fare::compareTo)
                .orElseThrow(() -> new CannotCalculateAdditionalFareException(stations));
    }
}
