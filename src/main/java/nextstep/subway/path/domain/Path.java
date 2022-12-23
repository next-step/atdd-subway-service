package nextstep.subway.path.domain;

import java.util.List;

public class Path {
    private final List<Long> stationIds;
    private final List<SectionEdge> sectionEdges;
    private final int distance;

    public Path(List<Long> stationIds, int distance, List<SectionEdge> sectionEdges ) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.sectionEdges = sectionEdges;
    }

    public static Path of(List<Long> stationIds, int distance, List<SectionEdge> sectionEdges) {
        return new Path(stationIds, distance, sectionEdges);
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public List<SectionEdge> getEdges(){
        return sectionEdges;
    }

}