package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final int price;

    private Path(List<Station> stations, int distance, int price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public static Path of(List<Station> stations, int distance, int price) {
        return new Path(stations, distance, price);
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance, 0);
    }

    public int getPathSize() {
        return stations.size();
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}
