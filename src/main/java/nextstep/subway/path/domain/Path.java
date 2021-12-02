package nextstep.subway.path.domain;
import nextstep.subway.fee.DiscountFeeByAge;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;


public class Path {

    private final Stations stations;
    private final int distance;
    private final int fee;


    public Path(Stations stations, int distance, int fee, int age) {
        this.stations = stations;
        this.distance = distance;
        this.fee = DiscountFeeByAge.getFee(age, fee);
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }

    public int getFee() {
        return fee;
    }
}
