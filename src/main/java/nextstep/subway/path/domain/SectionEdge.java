package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Station upStation;
    private final Station downStation;
    private final int distance;
    private final int lineFare;

    private SectionEdge(Station upStation, Station downStation, int distance, int lineFare) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineFare = lineFare;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section.getUpStation(), section.getDownStation(), section.getDistance(), section.getLineFare());
    }


    @Override
    protected double getWeight() {
        return distance;
    }

    @Override
    protected Object getSource() {
        return upStation;
    }

    @Override
    protected Object getTarget() {
        return downStation;
    }
}
