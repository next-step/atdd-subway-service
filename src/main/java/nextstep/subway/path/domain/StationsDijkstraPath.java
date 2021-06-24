package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.exception.path.PathException;
import nextstep.subway.station.domain.Station;

public class StationsDijkstraPath implements Path {

    private List<Station> stations;
    private List<SectionEdge> sectionEdges;

    public StationsDijkstraPath(List<Station> stations, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
    }

    @Override
    public int getMinDistance() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getDistance)
            .sum();
    }

    @Override
    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public static StationsDijkstraPath of(GraphPath<Station, SectionEdge> path) {
        validatoin(path);
        return new StationsDijkstraPath(path.getVertexList(), path.getEdgeList());
    }

    private static void validatoin(GraphPath<Station, SectionEdge> path) {
        if (path == null) {
            throw new PathException(PathException.NOT_CONNECTED);
        }
    }

}
