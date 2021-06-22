package nextstep.subway.path.domain;

import java.util.Collections;
import java.util.List;

import org.jgrapht.GraphPath;

import nextstep.subway.station.domain.Station;

public class Path {

    private List<Station> stations;
    private List<SectionEdge> sectionEdges;

    public Path(List<Station> stations, List<SectionEdge> sectionEdges) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
    }

    public int getMinDistance() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getDistance)
            .sum();
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public static Path of(GraphPath<Station, SectionEdge> path) {
        return new Path(path.getVertexList(), path.getEdgeList());
    }

}
