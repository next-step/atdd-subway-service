package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        if (this.distance < 1) {
            throw new RuntimeException("거리는 양수값 이어야 합니다.");
        }

        this.distance = distance;
    }

    public Distance subtract(int newDistance) {
        return new Distance(this.distance -= newDistance);
    }

    public int getDistance() {
        return distance;
    }

}
