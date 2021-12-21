package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathInfoService {
    public PathInfo calculatePathInfo(List<Line> lines, Station source, Station target) {
        GraphPath<Station, SectionWeightedEdge> shortestPath = PathFinder.of(lines).findShortestPath(source, target);
        Fare totalFare = FareCalculator.calculate(shortestPath.getWeight());


        return PathInfo.of(shortestPath, totalFare);
    }
}
