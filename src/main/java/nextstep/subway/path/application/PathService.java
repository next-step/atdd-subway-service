package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.FareCalculator;
import nextstep.subway.path.utils.PathFinder;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;

    private final LineRepository lineRepository;

    private final PathFinder pathFinder;

    private final FareCalculator fareCalculator;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder, FareCalculator fareCalculator) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Path path = pathFinder.findPath(lines, source, target);
        BigDecimal fare = fareCalculator.calculate(lines, path, loginMember.getAge());
        return PathResponse.from(path, fare);
    }
}
