package nextstep.subway.path;

import org.springframework.stereotype.Service;

@Service
public class PathService {

    public PathResponse searchShortestPath(Long source, Long target) {
        return new PathResponse();
    }
}
