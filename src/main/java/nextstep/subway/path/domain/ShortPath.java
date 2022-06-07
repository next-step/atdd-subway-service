package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.StreamUtils;

public class ShortPath {
    private List<Station> stations;
    private int distance;

    private ShortPath(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static ShortPath of(List<Station> stations, int distance) {
        return new ShortPath(stations, distance);
    }

    public PathResponse convertPathResponse() {
        return PathResponse.of(StreamUtils.mapToList(this.stations, StationResponse::of), this.distance);
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
