package nextstep.subway.path.dto;

import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath path) {
        List<StationResponse> stationResponses = path.getStations().stream().map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
        int distance = path.getDistance();

        return new PathResponse(stationResponses, distance);
    }

    public PathResponseAssembler() {
    }
}
