package nextstep.subway.line.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathGraph;
import nextstep.subway.line.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PathService {
    private LineRepository lineRepository;
    private PathGraph pathGraph;

    public PathService(LineRepository lineRepository, PathGraph pathGraph) {
        this.lineRepository = lineRepository;
        this.pathGraph = pathGraph;
    }

    public PathResponse path(Long sourceId, Long targetId) {
        return pathGraph.findPath(sourceId, targetId, lineRepository.findAll());
    }
}
