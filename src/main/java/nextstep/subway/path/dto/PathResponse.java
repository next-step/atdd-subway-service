package nextstep.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;

public class PathResponse {
    private List<StationDto> stations;
    private int distance;

    protected PathResponse() {
    }

    private PathResponse(List<StationDto> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static PathResponse from(Path path) {
        List<StationDto> stationDtos = path.getStations().stream()
                .map(StationDto::from)
                .collect(Collectors.toList());

        return new PathResponse(stationDtos, path.getDistance().value());
    }

    public List<StationDto> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
