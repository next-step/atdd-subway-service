package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    public PathResponse findShortestPath(Long source, Long target) {
        List<StationResponse> stations = new ArrayList<>();
        return new PathResponse(stations,10);
    }
}
