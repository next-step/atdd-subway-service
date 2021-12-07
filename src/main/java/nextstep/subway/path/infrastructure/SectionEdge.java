package nextstep.subway.path.infrastructure;


import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Station station;
    private final Station target;
    private final double weight;

    private SectionEdge(Station source, Station target, Integer weight) {
        this.station = source;
        this.target = target;
        this.weight = weight;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(),
            section.getDistance());
    }

    public List<Station> getVertexes() {
        return Arrays.asList(station, target);
    }

    @Override
    public Station getSource() {
        return station;
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
