package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathResultConvertor {
    public static PathResponse convert(PathResult pathResult) {
        return new PathResponse(getStationResponses(pathResult), pathResult.getDistance(), pathResult.getChargeValue());
    }

    private static List<StationResponse> getStationResponses(PathResult pathResult) {
        return pathResult.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
