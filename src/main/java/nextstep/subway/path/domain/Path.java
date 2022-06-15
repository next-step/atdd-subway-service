package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private int distance;
    private Fee fee;

    public Path(List<Station> stations, int distance, int addFee, DiscountType discountType) {
        this.stations = stations;
        this.distance = distance;
        this.fee = new Fee(addFee, distance, discountType);
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
