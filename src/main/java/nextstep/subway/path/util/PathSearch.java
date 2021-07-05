package nextstep.subway.path.util;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.fare.PolicyCalculator;
import nextstep.subway.fare.policy.Policies;
import nextstep.subway.fare.policy.discount.DiscountPolicy;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathDestination;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PathSearch {

    public PathResponse findPaths(AuthMember authMember, Lines lines, PathDestination pathDestination) {
        final GraphGenerator graphGenerator = new GraphGenerator(lines);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graphGenerator.getGraph());
        final GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(pathDestination.getSource(), pathDestination.getTarget());
        final Path path = new Path(graphPath);
        final int totalFare = getTotalFare(authMember, path);
        return new PathResponse(path, totalFare);
    }

    private int getTotalFare(AuthMember authMember, Path path) {
        List<DiscountPolicy> policies = new Policies(authMember).getDiscountPolicies();
        int totalFare = FareCalculator.calculateFare(path.getDistance());
        for (DiscountPolicy policy : policies) {
            totalFare = new PolicyCalculator(policy).calculate(totalFare, path.getMaxExtraFare());
        }
        return totalFare;
    }


}
