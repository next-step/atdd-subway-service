package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.path.dto.PathResponse;

@Service
@Transactional
public class PathService {

    public PathResponse findShortestPath(Long source, Long target) {
        return new PathResponse();
    }

}
