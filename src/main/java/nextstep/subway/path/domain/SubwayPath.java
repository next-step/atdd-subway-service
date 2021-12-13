package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;

public class SubwayPath {
    private List<Station> stations;
    private int distance;

    public SubwayPath() {
    }

    public SubwayPath(GraphPath path) {
        stations = path.getVertexList();
        distance = (int) path.getWeight();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
