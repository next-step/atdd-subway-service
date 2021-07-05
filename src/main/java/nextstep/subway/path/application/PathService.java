package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.fare.policy.customer.CustomerPolicyType;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

    private final MemberService memberService;
    private final LineService lineService;
    private final StationService stationService;

    public PathService(MemberService memberService,
            LineService lineService, StationService stationService) {
        this.memberService = memberService;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse getShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        List<Line> lines = lineService.findLinesEntities();
        PathFinder pathFinder = new PathFinder(lines);

        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);
        Path path = pathFinder.findPath(source, target);

        Fare totalFare = applyFarePolicies(loginMember, path);
        return PathResponse.of(path, totalFare);
    }

    private Fare applyFarePolicies(LoginMember loginMember, Path path) {
        Member member = memberService.findMemberEntity(loginMember.getId());

        List<FarePolicy> pathPolicies = path.getPolicies();
        FarePolicy customerPolicy = CustomerPolicyType.of(member).getPolicy();

        return Fare.DEFAULT.apply(pathPolicies).apply(customerPolicy);
    }
}
