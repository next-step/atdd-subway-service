package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class PathInfo {
    private GraphPath<Station, SectionWeightedEdge> shortestPath;
    private Fare totalFare;

    public PathInfo(GraphPath<Station, SectionWeightedEdge> shortestPath, Fare totalFare) {
        this.shortestPath = shortestPath;
        this.totalFare = totalFare;
    }

    public static PathInfo of(GraphPath<Station, SectionWeightedEdge> shortestPath, Fare totalFare) {
        return new PathInfo(shortestPath, totalFare);
    }

    public GraphPath<Station, SectionWeightedEdge> getShortestPath() {
        return shortestPath;
    }

    public Fare getTotalFare() {
        return totalFare;
    }

    public double getDistance() {
        return shortestPath.getWeight();
    }
}
