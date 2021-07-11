package nextstep.subway.path.application;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.JgraphtFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(AuthMember loginMember, Long sourceId, Long targetId) {
        Lines lines = Lines.of(lineRepository.findAll());
        Sections sections = lines.findSections();
        PathFinder pathFinder = new JgraphtFinder(sections);

        PathStation source = PathStation.of(lines.findStation(sourceId));
        PathStation target = PathStation.of(lines.findStation(targetId));

        List<PathStation> shortestPath = pathFinder.findPath(source, target);
        Distance shortestDistance = pathFinder.findShortestDistance(source, target);
        return PathResponse.of(shortestPath, shortestDistance, lines.calculatedFee(loginMember, shortestDistance));
    }
}