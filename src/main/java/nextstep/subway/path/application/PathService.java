package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse getShortestPath(Long source, Long target) {
        lineRepository.findAll();
        return new PathResponse("ABC");
    }
}
