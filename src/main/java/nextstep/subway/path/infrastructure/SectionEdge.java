package nextstep.subway.path.infrastructure;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Station source;
    private final Station target;
    private final double weight;

    private SectionEdge(Station source, Station target, Integer weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(),
            section.getDistance());
    }

    public static List<SectionEdge> toList(List<Section> sections) {
        return sections.stream()
            .map(SectionEdge::of)
            .collect(Collectors.toList());
    }

    public List<Station> getVertexes() {
        return Arrays.asList(source, target);
    }

    @Override
    public Station getSource() {
        return source;
    }

    @Override
    public Station getTarget() {
        return target;
    }

    @Override
    public double getWeight() {
        return weight;
    }

}
