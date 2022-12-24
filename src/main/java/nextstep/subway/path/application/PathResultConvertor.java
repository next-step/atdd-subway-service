package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Charge;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

public class PathResultConvertor {
    public static PathResponse convert(PathResult pathResult) {
        Charge charge = new Charge(pathResult.getDistance(), pathResult.getLines());

        return new PathResponse(getStationResponses(pathResult), pathResult.getDistance(), charge.value());
    }

    public static PathResponse convert(PathResult pathResult, LoginMember loginMember) {
        Charge charge = new Charge(pathResult.getDistance(), pathResult.getLines(), loginMember.getAge());

        return new PathResponse(getStationResponses(pathResult), pathResult.getDistance(), charge.value());
    }

    private static List<StationResponse> getStationResponses(PathResult pathResult) {
        return pathResult.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
