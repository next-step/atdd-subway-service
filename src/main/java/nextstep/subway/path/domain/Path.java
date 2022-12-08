package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.Collections;
import java.util.List;

public class Path {
    private final List<SectionEdge> sectionEdges;
    private final List<Station> stationPaths;
    private final Integer distance;

    private Path(List<SectionEdge> sectionEdges, List<Station> stationPaths, Integer distance) {
        this.sectionEdges = sectionEdges;
        this.stationPaths = stationPaths;
        this.distance = distance;
    }

    public static Path of(List<SectionEdge> sectionEdges, List<Station> stationPaths, Integer distance) {
        return new Path(sectionEdges, stationPaths, distance);
    }

    public List<SectionEdge> getSectionEdges() {
        return Collections.unmodifiableList(sectionEdges);
    }

    public List<Station> getStationPaths() {
        return Collections.unmodifiableList(stationPaths);
    }

    public Integer getDistance() {
        return distance;
    }
}
