package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class LinePathResponse {
    private List<StationResponse> stations;
    private int distance;

    public LinePathResponse() {
    }

    public LinePathResponse(Stations stations, Distance distance) {
        this.distance = distance.toInt();
        this.stations = new StationResponses(stations).toCollection();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
