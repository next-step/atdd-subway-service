package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {

    private final List<Station> stations;
    private final Distance distance;

    private Path(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.stations = graphPath.getVertexList();
        this.distance = Distance.of(graphPath.getWeight());
    }

    public static Path of(final GraphPath<Station, DefaultWeightedEdge> graphPath) {
        return new Path(graphPath);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Distance getDistance() {
        return distance;
    }
}
