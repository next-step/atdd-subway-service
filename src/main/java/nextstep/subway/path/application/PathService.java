package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.PathFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;

    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Path path = pathFinder.findPath(lines, source, target);
        return PathResponse.from(path);
    }
}
