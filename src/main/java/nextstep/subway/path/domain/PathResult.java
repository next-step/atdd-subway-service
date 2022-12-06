package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResult {

    private List<Station> stations;
    private double weight;

    public PathResult(List<Station> stations, double weight) {
        this.stations = stations;
        this.weight = weight;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<String> getStationNames() {
        return this.stations.stream().map(Station::getName).collect(Collectors.toList());
    }

    public double getWeight() {
        return this.weight;
    }
}
