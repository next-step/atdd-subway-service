package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class TempDistance {

    private int distance;

    public TempDistance(int distance) {
        this.distance = distance;
    }

    protected TempDistance() {

    }
}
