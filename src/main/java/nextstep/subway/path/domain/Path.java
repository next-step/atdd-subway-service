package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Money;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private Fare fare;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public void calculateFare(LoginMember member, List<Line> extraFareLines) {
        this.fare = new Fare();
        this.fare = fare.extraFare(distance, getLineFare(extraFareLines));
        if (!member.isGuest()) {
            this.fare = fare.discount(member.getAge());
        }
    }

    private int getLineFare(List<Line> extraFareLines) {
        return extraFareLines.stream()
                .map(Line::getMoney)
                .mapToInt(Money::getMoney)
                .max()
                .orElse(0);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }
}
