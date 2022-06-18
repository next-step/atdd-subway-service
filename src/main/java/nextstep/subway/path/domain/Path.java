package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

import static nextstep.subway.path.domain.BaseCharge.BASE_CHARGE;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private List<Line> lines;

    public Path(List<Station> stations, int distance, List<Line> allLines) {
        this.stations = stations;
        this.distance = distance;
    }

    public Charge findCharge() {
        return Charges.of(BASE_CHARGE, DistanceSurcharge.from(distance))
                .sum();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
