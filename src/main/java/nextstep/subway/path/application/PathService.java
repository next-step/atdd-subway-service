package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PathService {
    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        return new PathResponse(new ArrayList<>(), 10);
    }
}
