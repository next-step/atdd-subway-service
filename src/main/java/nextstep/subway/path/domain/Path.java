package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class Path {

    private List<Station> stations;
    private Distance distance;

    private Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = Distance.from(distance);
    }

    public static Path of(GraphPath graphPath) {
        Double totalDistance = graphPath.getWeight();
        return new Path(graphPath.getVertexList(), totalDistance.intValue());
    }



    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}
