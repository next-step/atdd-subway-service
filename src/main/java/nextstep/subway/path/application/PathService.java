package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(int source, int target) {
        List<Line> lines = lineRepository.findAll();
        return null;
    }
}