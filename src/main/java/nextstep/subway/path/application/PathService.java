package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class PathService {

    public PathResponse findShortestRoute(long source, long target) {
        return new PathResponse(
                Arrays.asList(
                        new StationResponse(1L, "양재역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(2L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now()),
                        new StationResponse(3L, "교대역", LocalDateTime.now(), LocalDateTime.now())
                ),
                5
        );
    }
}
