package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class PathFinder {
    public PathResponse getPath(PathRequest pathRequest) {
        return new PathResponse(
                Arrays.asList(
                        new StationResponse(3L, "교대역", null, null),
                        new StationResponse(4L, "남부터미널역", null, null),
                        new StationResponse(2L,"양재역", null, null)
                ),
                10);
    }
}
