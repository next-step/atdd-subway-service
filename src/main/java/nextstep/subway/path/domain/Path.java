package nextstep.subway.path.domain;

import nextstep.subway.line.domain.AdditionalFare;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {
    private static final int INITIAL_ADDITIONAL_FARE = 0;

    private final List<Station> stations;
    private final int distance;
    private final AdditionalFare additionalFare;

    private Path (List<Station> stations, int distance, int additionalFare) {
        this.stations = stations;
        this.distance = distance;
        this.additionalFare = AdditionalFare.from(additionalFare);
    }

    private Path (List<Station> stations, int distance) {
        this(stations, distance, INITIAL_ADDITIONAL_FARE);
    }

    public static Path of(List<Station> stations, int distance) {
        return new Path(stations, distance);
    }

    public static Path of(List<Station> stations, int distance, int additionalFare) {
        return new Path(stations, distance, additionalFare);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getAdditionalFare() {
        return additionalFare.getFare();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Path path = (Path) obj;
        return Objects.equals(stations, path.stations)
                && distance == path.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
