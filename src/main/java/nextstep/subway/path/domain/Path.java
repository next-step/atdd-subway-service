package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public final class Path {

    private final List<Station> stations;
    private final Integer totalDistance;

    private Path(GraphPath<Station, SectionEdge> graphPath) {
        this.stations = graphPath.getVertexList();
        this.totalDistance = Double.valueOf(graphPath.getWeight()).intValue();
    }

    public static Path from(GraphPath graphPath) {
        return new Path(graphPath);
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }
}
