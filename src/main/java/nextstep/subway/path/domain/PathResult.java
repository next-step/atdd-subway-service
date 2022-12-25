package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import nextstep.subway.line.domain.Charge;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathResult {
    private List<Station> stations;
    private int distance;

    private Charge charge;

    public PathResult(List<Station> stations, int distance, Set<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.charge = new Charge(distance, lines);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public Charge getCharge() {
        return charge;
    }

    public int getChargeValue() {
        return getCharge().value();
    }

    public void discountCharge(int age) {
        charge.discount(age);
    }
}

