package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class LinePathResponse {
    private List<StationResponse> stations;
    private int distance;

    public LinePathResponse() {
    }

    public LinePathResponse(Line shortDistance, Station source, Station target) {
        this.distance = shortDistance.calcDistanceBetween(source, target).toInt();
        this.stations = new StationResponses(shortDistance.findShortestRoute(source, target))
                .toCollection();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
