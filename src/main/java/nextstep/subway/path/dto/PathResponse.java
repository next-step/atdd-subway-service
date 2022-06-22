package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private  List<StationResponse> stations;
    private  long  distance;
    private  long extraCharge;

    public PathResponse() {
    }
    public PathResponse(List<StationResponse> stations, long distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stations, long distance, long extraCharge) {
        this.stations = stations;
        this.distance = distance;
        this.extraCharge = extraCharge;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public long getExtraCharge() {
        return extraCharge;
    }

    public static PathResponse of(final Sections sections) {
        return new PathResponse(
                sections.getStations().stream().map(StationResponse::of)
                .collect(Collectors.toList()),
                sections.getTotalDistance().of(),
                sections.totalCharge().of());
    }
}
