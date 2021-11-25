package nextstep.subway.path.application;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.path.dto.PathResponse;

@Service
@Transactional
public class PathService {

    public PathResponse findShortPath(Long sourceId, Long targetId) {
        return PathResponse.of(Collections.emptyList(), 1);
    }
}
