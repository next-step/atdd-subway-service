package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private int distance;
    private Fee fee;

    public Path(List<Station> stations, int distance, int addFee, double discountPercent) {
        this.stations = stations;
        this.distance = distance;
        this.fee = new Fee(addFee, distance, discountPercent);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee.calculate();
    }
}
