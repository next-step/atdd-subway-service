package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.Policies;
import nextstep.subway.path.domain.policy.discount.DiscountPolicyFactory;
import nextstep.subway.path.domain.policy.distance.DistancePolicyFactory;
import nextstep.subway.path.domain.policy.line.LinePolicyFactory;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(PathRequest pathRequest, LoginMember loginMember) {
        Lines lines = findLines();
        Station source = lines.findStationById(pathRequest.getSource());
        Station target = lines.findStationById(pathRequest.getTarget());
        PathFinder pathFinder = new StationPathFinder(
                new DijkstraShortestPath(new SubwayMapData(lines, new WeightedMultigraph(SectionEdge.class)).initData()),
                new Direction(source, target));
        PathResult paths = pathFinder.findPaths();
        Fare fare = new Fare();
        Policies policies = initPolicies(paths, loginMember);
        int calculatedFare = policies.calculate(fare.getFareValue());

        return PathResponse.of(paths, calculatedFare);
    }

    private Policies initPolicies(PathResult paths, LoginMember loginMember) {
        FarePolicy distancePolicy = DistancePolicyFactory.findPolicy(paths.getTotalDistance());
        FarePolicy lineFarePolicy = LinePolicyFactory.findPolicy(paths.getMaxLineFare());
        FarePolicy discountPolicy = DiscountPolicyFactory.findPolicy(loginMember.getAge());
        return Policies.of(distancePolicy, lineFarePolicy, discountPolicy);
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }
}
