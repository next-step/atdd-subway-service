package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private Surcharge maxSurcharge;

    public Path(List<Station> stations, Distance distance, Surcharge maxSurcharge) {
        this.stations = stations;
        this.distance = distance;
        this.maxSurcharge = maxSurcharge;
    }

    public List<Station> getStations() {
        return stations;
    }
    public Distance getDistance() {
        return distance;
    }
    public Surcharge getMaxSurcharge() {
        return maxSurcharge;
    }
}
