package nextstep.subway.path.dto;

import java.util.Collections;
import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.station.domain.Station;

public class PathResponse {

    private List<Station> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(Path path, Fare fare) {
        return new PathResponse(path.getStations(), path.getDistance().getDistance(), fare.getFare());
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
