package nextstep.subway.path.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.init(lines);

        if (loginMember.isLoggedIn()) {
            return PathResponse.of(loginMember.createAgeDiscount(), pathFinder.findShortestPath(source, target));
        }
        return PathResponse.from(pathFinder.findShortestPath(source, target));
    }
}
