package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    
    private Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }
    public static Path valueOf(List<Station> stations, Distance distance) {
        return new Path(stations, distance);
    }

    public List<Station> stations() {
        return stations;
    }

    public Distance distance() {
        return distance;
    }

    public int distanceValue() {
        return distance.distance();
    }
}
