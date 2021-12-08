package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.path.policy.DiscountPolicy;
import nextstep.subway.path.policy.DiscountPolicyByAgeResolver;
import nextstep.subway.path.policy.FarePolicy;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;
    private final FarePolicy farePolicy;
    private final DiscountPolicyByAgeResolver discountPolicyByAgeResolver;

    public PathService(StationService stationService, LineService lineService, PathFinder pathFinder, FarePolicy farePolicy, DiscountPolicyByAgeResolver discountPolicyByAgeResolver) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.farePolicy = farePolicy;
        this.discountPolicyByAgeResolver = discountPolicyByAgeResolver;
    }

    public PathResponse getShortCut(Long sourceStationId, Long targetStationId, LoginMember loginMember) {
        validateParameters(sourceStationId, targetStationId);

        Station source = stationService.findStationById(sourceStationId);
        Station target = stationService.findStationById(targetStationId);

        Set<Line> lines = new HashSet<>(lineService.findLineByStation(source));
        lines.addAll(lineService.findLineByStation(target));

        PathResult shortCut = findShortCut(source, target, lines);
        int fare = getFare(loginMember, lines, shortCut);

        return PathResponse.of(shortCut, fare);
    }

    private int getFare(LoginMember loginMember, Set<Line> lines, PathResult shortCut) {
        int fare = farePolicy.calculateFare(shortCut.getMaxExtraFare(), (int) shortCut.getWeight());
        if (!loginMember.isEmpty()) {
            DiscountPolicy discountPolicy = discountPolicyByAgeResolver.resolve(loginMember.getAge());
            fare = discountPolicy.apply(fare);
        }
        return fare;
    }

    private void validateParameters(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new ServiceException("출발지와 목적지가 같습니다.");
        }
    }

    private PathResult findShortCut(Station source, Station target, Set<Line> lines) {
        PathResult shortCut = pathFinder.findShortCut(lines, source, target);
        if (shortCut.isEmpty()) {
            throw new ServiceException("최단 경로를 찾을 수 없습니다");
        }
        return shortCut;
    }
}
