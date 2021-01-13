package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;


public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    protected PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public static PathResponse of(GraphPath<Station, Section> path, int fare) {
        List<StationResponse> stationResponses = path.getVertexList().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(
                stationResponses,
                (int) path.getWeight(),
                fare
        );
    }
}
