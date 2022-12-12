package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PathService {
    public PathResponse findTheShortestPath(long sourceStationId, long targetStationId) {
        return null;
    }
}
