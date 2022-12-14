package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SurCharge;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private SurCharge maxSurCharge;

    public Path(List<Station> stations, Distance distance, SurCharge maxSurCharge) {
        this.stations = stations;
        this.distance = distance;
        this.maxSurCharge = maxSurCharge;
    }

    public List<Station> getStations() {
        return stations;
    }
    public Distance getDistance() {
        return distance;
    }
    public SurCharge getMaxSurCharge() {
        return maxSurCharge;
    }
}
