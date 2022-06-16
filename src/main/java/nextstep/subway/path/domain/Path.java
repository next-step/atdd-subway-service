package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Integer distance;
    private final Integer surcharge;

    public Path(List<Station> stations, Integer distance, Integer surcharge) {
        this.stations = stations;
        this.distance = distance;
        this.surcharge = surcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getSurcharge() {
        return surcharge;
    }

}
