package nextstep.subway.path.domain;

import nextstep.subway.fare.Fare;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final Lines lines;
    private final List<Station> stations;
    private final int distance;
    private Fare fare;

    public Path(List<Station> stations, int distance, Lines lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Lines getLines() {
        return lines;
    }

    public Fare getFare() {
        return fare;
    }

    public void updateFare(Fare targetFare) {
        this.fare = targetFare;
    }
}
