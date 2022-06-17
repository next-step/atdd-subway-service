package nextstep.subway.path.application;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private PathFinder pathFinder;

    @Autowired
    public PathService(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        return pathFinder.findShortestPath(source, target);
    }
}
