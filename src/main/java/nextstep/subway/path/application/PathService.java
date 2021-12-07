package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PathService {
    public PathResponse findShortestPath(long source, long target) {
        List<StationResponse> stations = Arrays.asList(
                new StationResponse(1L, "강남역", LocalDateTime.now(), null),
                new StationResponse(2L, "양재역", LocalDateTime.now(), null),
                new StationResponse(3L, "남부터미널역", LocalDateTime.now(), null));

        return new PathResponse(stations, 13);
    }
}
