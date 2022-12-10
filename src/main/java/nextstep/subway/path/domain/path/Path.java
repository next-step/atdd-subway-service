package nextstep.subway.path.domain.path;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {

    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    private Path(List<Station> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = Distance.from(distance);
        this.fare = fare;
    }

    public static Path of(GraphPath graphPath, Fare fare) {
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), fare);
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


}
