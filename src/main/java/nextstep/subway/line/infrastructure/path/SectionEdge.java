package nextstep.subway.line.infrastructure.path;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.domain.Money;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Money additionalFare;
    private final Station source;
    private final Station target;
    private final double weight;

    private SectionEdge(Money additionalFare, Station source, Station target, Integer weight) {
        this.additionalFare = additionalFare;
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getLineAdditionalFare(), section.getUpStation(),
            section.getDownStation(), section.getDistance());
    }

    public static List<SectionEdge> toList(Sections sections) {
        return sections.getSections()
            .stream()
            .map(SectionEdge::of)
            .collect(Collectors.toList());
    }

    public List<Station> getVertexes() {
        return Arrays.asList(source, target);
    }

    public Money getAdditionalFare() {
        return additionalFare;
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
