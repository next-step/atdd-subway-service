package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResult {

    private List<Station> stations;
    private Distance distance;

    private Money maxLineCharge;

    public PathResult(List<Station> stations, Distance distance, Money maxLineCharge) {
        this.stations = stations;
        this.distance = distance;
        this.maxLineCharge = maxLineCharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<String> getStationNames() {
        return this.stations.stream().map(Station::getName).collect(Collectors.toList());
    }

    public Distance getDistance() {
        return distance;
    }

    public double getWeight() {
        return this.distance.getDistance();
    }

    public Money getMaxLineCharge() {
        return maxLineCharge;
    }
}
