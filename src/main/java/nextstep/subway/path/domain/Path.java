package nextstep.subway.path.domain;

import static java.util.Collections.*;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final double distance;

    Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(GraphPath<Station, Section> path) {
        return new Path(path.getVertexList(), path.getWeight());
    }

    public double getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return unmodifiableList(stations);
    }
}
