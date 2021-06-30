package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class FareCalculator {

    public static FareCalculator newInstance() {
        return new FareCalculator();
    }

    public int calculateFare(GraphPath<Station, SectionEdge> path, int distance){
        int fare = DistanceFare.findDistanceFareByDistance(distance).calculateFare(distance);
        Integer extraFare = path.getEdgeList().stream()
                .map(SectionEdge::getCharge)
                .max(Integer::compareTo)
                .orElseThrow(IllegalAccessError::new);
        return fare + extraFare;
    }
}
