package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;
    private int fee;

    public PathResponse(List<StationResponse> stations, int distance, int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static PathResponse of(PathFinder pathFinder) {
        return new PathResponse(pathFinder.getShortestPathStationList().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), pathFinder.getShortestPathDistance(), pathFinder.getFee());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

}
