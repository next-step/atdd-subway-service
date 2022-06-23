package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.generic.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;

    private int distance;


    public PathResponse(List<StationResponse> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance.getValue();
    }

    public static PathResponse of(Sections sections) {
        return new PathResponse(sections.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), sections.totalDistance());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
