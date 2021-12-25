package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.Member;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathInfoService {
    public PathInfo calculatePathInfo(List<Line> lines, Station source, Station target, Member loginMember) {
        GraphPath<Station, SectionWeightedEdge> shortestPath = PathFinder.of(lines).findShortestPath(source, target);

        Fare additionalFare = calculateAdditionalFare(shortestPath);

        return PathInfo.of(shortestPath, totalFare(loginMember, additionalFare));
    }

    private Fare calculateAdditionalFare(GraphPath<Station, SectionWeightedEdge> shortestPath) {

        Fare additionalFareByDistance = DistanceFareCalculator.calculateByDistance(shortestPath.getWeight());
        Fare additionalFareByLine = LineFareCalculator.calculateByLine(shortestPath.getEdgeList());

        return additionalFareByDistance.plus(additionalFareByLine);
    }

    private Fare totalFare(Member loginMember, Fare additionalFare) {
        Fare fare = additionalFare.plusDefaultFare();
        return AgeFareCalculator.calculateByAge(loginMember, fare);
    }
}
