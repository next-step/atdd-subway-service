package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;
    private final Station source;
    private final Station target;
    private final double weight;
    private Fare lineFare;

    public SectionEdge(Section section, Station source, Station target, int weight, Fare lineFare) {
        this(section, source, target, weight);
        this.lineFare = lineFare;
    }

    public SectionEdge(Section section, Station source, Station target, int weight) {
        this.section = section;
        this.source = source;
        this.target = target;
        this.weight = Double.valueOf(weight);
    }

    public int distance() {
        return (int) weight;
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

    public Section getSection() {
        return section;
    }

    public Fare getLineFare(){
        return lineFare;
    }
}
