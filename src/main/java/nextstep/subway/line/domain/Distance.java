package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column
    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.distance = distance;
    }

    public Distance minus(Distance distance) {
        return new Distance(this.distance - distance.distance);
    }

    public Distance plus(Distance distance) {
        return new Distance(this.distance + distance.distance);
    }

    public int getDistance() {
        return distance;
    }
}
