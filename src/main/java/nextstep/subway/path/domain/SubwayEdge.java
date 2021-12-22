package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayEdge extends DefaultWeightedEdge {
    private Section section;

    private SubwayEdge(Section section) {
        this.section = section;
    }

    public static SubwayEdge of(Section section) {
        return new SubwayEdge(section);
    }

    public static List<Section> toSections(List<SubwayEdge> subwayEdges) {
        return subwayEdges.stream()
                .map(it -> it.getSection())
                .collect(Collectors.toList());
    }

    private Section getSection() {
        return this.section;
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public Station getDownStation() {
        return section.getDownStation();
    }

    public int getDistance() {
        return section.getDistance();
    }
}
