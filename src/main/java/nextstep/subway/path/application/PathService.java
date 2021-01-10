package nextstep.subway.path.application;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Money;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.calcurator.Discount;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.path.exception.StationNotRegisteredException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository,
            StationService stationService,
            PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(PathRequest request, LoginMember loginMember) {

        validateRequest(request);

        Lines lines = new Lines(lineRepository.findAll());
        Station sourceStation = stationService.findById(request.getSource());
        Station targetStation = stationService.findById(request.getTarget());

        validateContains(lines, sourceStation, targetStation, request);

        Path path = pathFinder.findPath(lines, sourceStation, targetStation);

        Money money = Discount.discountCalculate(path.getDistance(),
                path.getMaxAdditionalFare(),
                loginMember.getAge());

        return PathResponse.of(path, money);
    }

    private void validateRequest(PathRequest request) {
        if (request.getSource().equals(request.getTarget())) {
            throw new IllegalArgumentException("같은 역은 조회할 수 없습니다.");
        }
    }

    private void validateContains(Lines lines, Station sourceStation, Station targetStation, PathRequest request) {
        if (!lines.contains(sourceStation)) {
            throw new StationNotRegisteredException(request.getSource());
        }
        if (!lines.contains(targetStation)) {
            throw new StationNotRegisteredException(request.getTarget());
        }
    }

}
