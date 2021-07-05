package nextstep.subway.station.dto;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Path;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public static PathResponse of(GraphPath graphPath) {
        List<Station> paths = graphPath.getVertexList();
        List<StationResponse> stations = StationResponse.asList(paths);
        return new PathResponse(stations, (int)graphPath.getWeight());
    }

    public static PathResponse of(Path path, LoginMember loginMember) {
        List<StationResponse> stations = StationResponse.asList(path.stations());
        return new PathResponse(stations, path.distance(), path.fare(loginMember));
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    private PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public PathResponse() {
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
