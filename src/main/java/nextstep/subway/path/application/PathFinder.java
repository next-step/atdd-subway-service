package nextstep.subway.path.application;

import nextstep.subway.path.dto.ShortestPath;
import org.springframework.stereotype.Service;


@Service
public class PathFinder {

    public ShortestPath findShortestPath(Long source, Long target) {

        return new ShortestPath();
    }
}
