package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(final List<Station> stations, final int distance) {
        this.stations = new ArrayList<>(stations);
        this.distance = distance;
    }

    public Fare calculateFare() {
        final Fare fare = Fare.of();
        return fare.calculateOverFare(new FareDistance(distance));
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
