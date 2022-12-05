package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final int DISTANCE_ZERO = 0;

    @Column(name = "distance")
    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance from(int distance) {
        if (distance <= DISTANCE_ZERO) {
            throw new IllegalArgumentException("역 사이의 거리는 0 이하일 수 없습니다.");
        }
        return new Distance(distance);
    }

    public Distance subtract(Distance distance) {
        if (this.distance <= distance.distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return Distance.from(this.distance - distance.distance);
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.getDistance());
    }

    public int getDistance() {
        return distance;
    }
}
