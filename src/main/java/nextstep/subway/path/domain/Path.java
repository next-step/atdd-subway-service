package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    private Path(List<Station> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path of(GraphPath graphPath) {
        Distance distance = Distance.from((int)graphPath.getWeight());
        int maxSurCharge = getMaxSurCharge(graphPath.getEdgeList());
        return new Path(graphPath.getVertexList(), distance, Fare.of(distance, maxSurCharge));
    }

    public static Path ofAge(GraphPath graphPath, Integer age) {
        Distance distance = Distance.from((int)graphPath.getWeight());
        int maxSurCharge = getMaxSurCharge(graphPath.getEdgeList());
        Discount discount = Discount.getDiscountByAge(age.intValue());
        return new Path(graphPath.getVertexList(), distance, Fare.ofDiscount(distance, maxSurCharge, discount));
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }
    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }

    private static int getMaxSurCharge(List<SectionEdge> sections){

        return sections.stream().mapToInt(
                SectionEdge::getLineSurCharge
        ).max().orElse(0);
    }
}
