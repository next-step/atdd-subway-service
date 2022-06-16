package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

import java.util.List;

public class Path {
    private List<Station> stations;
    private Integer pathLength;
    private Integer pathSurcharge;

    public Path(List<Station> stations, Integer pathLength, Integer pathSurcharge) {
        this.stations = stations;
        this.pathLength = pathLength;
        this.pathSurcharge = pathSurcharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getPathLength() {
        return pathLength;
    }

    public Integer getPathSurcharge() {
        return pathSurcharge;
    }
}
