package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fee;

import java.util.List;

public class PathResponse {
    private List<PathStation> stations;
    private int distance;
    private int fee;

    protected PathResponse() {}

    private PathResponse(List<PathStation> stations, Distance distance, Fee fee) {
        this.stations = stations;
        this.distance = distance.get();
        this.fee = fee.get();
    }

    public static PathResponse of(List<PathStation> stations, Distance distance, Fee fee) {
        return new PathResponse(stations, distance, fee);
    }

    public List<PathStation> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
