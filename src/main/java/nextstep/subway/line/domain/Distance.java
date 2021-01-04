package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() {}

    public Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 1보다 작을 수 없습니다.");
        }
    }

    public int get() {
        return distance;
    }
}
