package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {
    private final GraphPath<Station, SectionEdge> graphPath;

    public Path(GraphPath<Station, SectionEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public static Path of(GraphPath<Station, SectionEdge> graphPath) {
        return new Path(graphPath);
    }

    public Integer calculateFare(LoginMember loginMember) {
        Integer fare = getLineOverFare();
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.findByDistance(getDistance());
        fare += distanceFarePolicy.calculate(getDistance());
        AgeFarePolicy ageFarePolicy = AgeFarePolicy.findByAge(loginMember.getAge());
        return ageFarePolicy.calculate(fare);
    }

    private Integer getLineOverFare() {
        return graphPath.getEdgeList().stream()
                .map(SectionEdge::getLineOverFare)
                .max(Integer::compareTo)
                .orElse(Line.OVERFARE_MIN);
    }

    public List<Station> getStations() {
        return graphPath.getVertexList();
    }

    public Integer getDistance() {
        return (int) graphPath.getWeight();
    }
}
