package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Long distance;
    private Fare fare;

    public Path(List<Station> stations, Long distance) {
        this.stations = stations;
        this.distance = distance;
        this.fare = Fare.from(distance);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Long getDistance() {
        return distance;
    }

    public BigInteger getFare() {
        return fare.getFare();
    }
}
