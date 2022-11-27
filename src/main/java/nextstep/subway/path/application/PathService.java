package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.ui.PathResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.from(lines);
        return pathFinder.getShortestPath(sourceId, targetId);
    }
}
