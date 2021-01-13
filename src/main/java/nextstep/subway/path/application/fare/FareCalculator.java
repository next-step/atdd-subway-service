package nextstep.subway.path.application.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FareCalculator {

    private static final int BASIC_FARE = 1_250;

    private DistanceOverFarePolicy getDistanceFarePolicy(int distance) {
        return DistanceOverFarePolicy.valueOf(distance);
    }

    private int getMaxLineOverFare(List<Section> sections) {
        return sections.stream()
                .mapToInt(section -> section.getLine().getOverFare())
                .max().orElse(0);
    }

    public int calculateFare(GraphPath<Station, Section> path, LoginMember loginMember) {
        int distance = (int) path.getWeight();
        DistanceOverFarePolicy distanceFarePolicy = getDistanceFarePolicy(distance);

        int fare = BASIC_FARE
                + distanceFarePolicy.calculateOverFare(distance)
                + getMaxLineOverFare(path.getEdgeList());

        return fare - loginMember.getDrawbackFare(fare);
    }
}
