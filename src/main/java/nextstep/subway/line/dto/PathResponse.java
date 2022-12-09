package nextstep.subway.line.dto;

import nextstep.subway.path.dto.StationPathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationPathResponse> stations;

    protected PathResponse() {

    }

    private PathResponse(List<StationPathResponse> stations) {
        this.stations = stations;
    }

    public static PathResponse from(List<Station> stationsInPath) {
        List<StationPathResponse> collect = stationsInPath.stream()
                .map(StationPathResponse::from)
                .collect(Collectors.toList());
        return new PathResponse(collect);
    }

    public List<StationPathResponse> getStations() {
        return this.stations;
    }
}
