package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

import org.jgrapht.GraphPath;

import nextstep.subway.station.domain.Station;

public class Path {
    private final GraphPath graphPath;

    public Path(GraphPath graphPath) {
        validateNullObject(graphPath);
        this.graphPath = graphPath;
    }

    public List<Station> getStations() {
        return graphPath.getVertexList();
    }

    public int getTotalDistance() {
        return (int) graphPath.getWeight();
    }

    private void validateNullObject(GraphPath graphPath) {
        if (Objects.isNull(graphPath)) {
            throw new IllegalStateException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }
}
