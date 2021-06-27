package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(GraphPath<Station, SectionEdge> pathResult) {
        if (pathResult == null) {
            throw new IllegalArgumentException(Message.ERROR_PATH_NOT_FOUND.showText());
        }
        stations = pathResult.getVertexList();
        distance = (int) pathResult.getWeight();
    }

    public List<Station> getStations() {
        return this.stations;
    }

    public int getDistance() {
        return this.distance;
    }
}
