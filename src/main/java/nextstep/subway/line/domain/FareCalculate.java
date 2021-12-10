package nextstep.subway.line.domain;

import static java.util.Comparator.naturalOrder;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

public class FareCalculate {

    private Distance distance;

    public static FareCalculate of(Distance totalDistance) {
        return new FareCalculate(totalDistance);
    }

    public FareCalculate(Distance distance) {
        this.distance = distance;
    }

    public Fare calculate(List<Station> fastStations, List<Line> lines, LoginMember loginMember) {

        Fare surcharge = getSurcharge(fastStations, lines);

        FarePolicy subwayPolicy = FarePolicyFactory.create(loginMember);
        Fare policyFare = subwayPolicy.calculateFare(distance);

        return Fare.sum(policyFare, surcharge);
    }

    public Fare getSurcharge(List<Station> fastStations, List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getSections().stream())
            .filter(section -> findStationByStationName(fastStations, section))
            .map(Section::getLine)
            .map(Line::getSurcharge)
            .max(naturalOrder())
            .map(Fare::new)
            .orElse(new Fare(0L));
    }

    private boolean findStationByStationName(List<Station> fastStations, Section section) {
        return fastStations.stream()
            .anyMatch(station -> station.equalsName(section.getUpStation()));
    }
}
