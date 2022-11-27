package nextstep.subway.path.application;

import nextstep.subway.path.ui.PathResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PathService {

    public List<PathResponse> findShortestPath(final Long sourceId, final Long targetId) {
        return Collections.emptyList();
    }
}
