package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int MINIMUM_DISTANCE = 1;

    @Column
    private int distance;

    public Distance() {
    }

    public Distance(final int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new RuntimeException(String.format("역과 역 사이의 거리는 최소 %d 이상입니다.", MINIMUM_DISTANCE));
        }
        this.distance = distance;
    }

    public Distance plus(int newDistance) {
        return new Distance(this.getDistance() + newDistance);
    }

    public Distance plus(Distance newDistance) {
        return plus(newDistance.distance);
    }

    public Distance minus(Distance newDistance) {
        return minus(newDistance.distance);
    }

    public Distance minus(int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.distance - newDistance);
    }

    public int getDistance() {
        return this.distance;
    }
}
