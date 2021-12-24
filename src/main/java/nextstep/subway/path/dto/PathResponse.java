package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponses;

import java.util.List;

public class PathResponse {

    private StationResponses stations;
    private Distance distance;

    public PathResponse() {

    }

    private PathResponse(StationResponses stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse of(final List<Station> stations, final Distance distance) {
        return new PathResponse(new StationResponses(stations), distance);
    }

    public StationResponses getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

}
