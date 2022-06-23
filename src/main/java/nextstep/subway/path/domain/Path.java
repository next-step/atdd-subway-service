package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final int surcharge;

    public Path(final List<Station> stations, final int distance, final int surcharge) {
        this.stations = stations;
        this.distance = distance;
        this.surcharge = surcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getSurcharge() {
        return surcharge;
    }
}
