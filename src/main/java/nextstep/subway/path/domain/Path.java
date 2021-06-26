package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.station.domain.Station;

public class Path {
    private final GraphPath graphPath;

    public Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<Station> getStations() {
        return graphPath.getVertexList();
    }

    public int getTotalDistance() {
        return (int) graphPath.getWeight();
    }
}
