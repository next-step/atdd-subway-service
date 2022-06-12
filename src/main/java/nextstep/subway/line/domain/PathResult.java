package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class PathResult {
    private List<Station> stations;
    private List<SectionWeightedEdge> sectionEdges;
    private int distance;
    private Fare fare;

    private PathResult(LoginMember loginMember, GraphPath graphPath) {
        this.stations = graphPath.getVertexList();
        this.sectionEdges = graphPath.getEdgeList();
        this.distance = culculateDistance();
        this.fare = Fare.of(distance, loginMember.getAge(), sectionEdges);
    }

    public static PathResult of(LoginMember loginMember, GraphPath graphPath) {
        return new PathResult(loginMember, graphPath);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return this.distance;
    }

    public int getFare() {
        return this.fare.getFare();
    }

    private int culculateDistance() {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }
}
