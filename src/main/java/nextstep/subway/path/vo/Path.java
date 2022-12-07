package nextstep.subway.path.vo;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
public class Path {
    private List<Station> stations;
    private int distance;

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance =  distance;
    }

    public static Path from(List<Station> vertexList, int weight) {
        return new Path(vertexList, weight);
    }


    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
