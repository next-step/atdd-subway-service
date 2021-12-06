package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final PathFactory pathFactory;

    public PathService(LineRepository lineRepository, PathFactory pathSearch) {
        this.lineRepository = lineRepository;
        this.pathFactory = pathSearch;
    }

    public PathResponse getShortestPath(Long source, Long target) {
        lineRepository.findAll();
        return new PathResponse("ABC");
    }
}
