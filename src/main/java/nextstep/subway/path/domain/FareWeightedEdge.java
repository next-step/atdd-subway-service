package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.jgrapht.graph.DefaultWeightedEdge;

public class FareWeightedEdge extends DefaultWeightedEdge {
    private Fare fare;

    public FareWeightedEdge(Fare fare) {
        this.fare = fare;
    }

    public Fare getFare() {
        return fare;
    }
}
