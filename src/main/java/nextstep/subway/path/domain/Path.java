package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Path {
    private final Stations stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = new Stations(stations);
        this.distance = distance;
    }

    public Stations getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStationResponses() {
        return stations.toStationResponses();
    }
}
