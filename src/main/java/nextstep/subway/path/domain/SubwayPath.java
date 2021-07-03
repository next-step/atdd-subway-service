package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Collections;
import java.util.List;

public class SubwayPath {

    private static final String NOT_CONNECTED_STATIONS_ERROR = "두 역이 연결되어 있지 않습니다.";

    private final List<Station> stations;
    private final List<SectionEdge> sectionEdges;

    public SubwayPath(GraphPath<Station, SectionEdge> path) {
        validateConnect(path);

        this.stations = path.getVertexList();
        this.sectionEdges = path.getEdgeList();
    }

    public List<Station> stations() {
        return Collections.unmodifiableList(stations);
    }

    public int distance() {
        return sectionEdges.stream().mapToInt(edge -> edge.section().distance()).sum();
    }

    private void validateConnect(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException(NOT_CONNECTED_STATIONS_ERROR);
        }
    }
}
