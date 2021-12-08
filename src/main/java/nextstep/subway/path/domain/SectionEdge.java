package nextstep.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public final class SectionEdge extends DefaultWeightedEdge {
    private final Station source;
    private final Station target;
    private final Distance distance;

    private SectionEdge(Section section) {
        this.source = section.getUpStation();
        this.target = section.getDownStation();
        this.distance = section.getDistance();
    }

    public static SectionEdge from(Section section) {
        return new SectionEdge(section);
    }

    public static List<SectionEdge> fromList(List<Section> sections) {
        return sections.stream()
            .map(it -> SectionEdge.from(it))
            .collect(Collectors.toList());
    }

    @Override
    public Station getSource() {
        return source;
    }

    @Override
    public Station getTarget() {
        return target;
    }

    public Integer getDistance() {
        return distance.get();
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
            "source=" + source +
            ", target=" + target +
            ", distance=" + distance +
            '}';
    }
}
