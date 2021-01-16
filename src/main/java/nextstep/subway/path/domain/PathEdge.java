package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathEdge extends DefaultWeightedEdge {
    private Station source;
    private Station target;
    private Distance distance;
    private Fare fare;

    private PathEdge(Station source, Station target, Distance distance, Fare fare) {
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathEdge of(Section section) {
        return new PathEdge(section.getUpStation(), section.getDownStation(), section.getDistance(), section.getFare());
    }

    public Fare getFare() {
        return fare;
    }

    @Override
    public final Station getSource() {
        return source;
    }

    @Override
    public final Station getTarget() {
        return target;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return distance.get();
    }

}
