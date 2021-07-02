package nextstep.subway.component.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class SubwayPath {
    private List<SectionWeightedEdge> sectionWeightedEdges;
    private List<Station> stations;

    public SubwayPath() {
    }

    public SubwayPath(List<SectionWeightedEdge> sectionWeightedEdges, List<Station> stations) {
        this.sectionWeightedEdges = sectionWeightedEdges;
        this.stations = stations;
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }

    public List<SectionWeightedEdge> getSectionWeightedEdges() {
        return new ArrayList<>(sectionWeightedEdges);
    }

    public int calcTotalDistance() {
        return sectionWeightedEdges
                .stream()
                .mapToInt(SectionWeightedEdge::sectionDistance)
                .sum();
    }
}
