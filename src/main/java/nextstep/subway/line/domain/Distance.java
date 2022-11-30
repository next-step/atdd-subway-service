package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column(nullable = false)
    private int distance;

    protected Distance() {}

    private Distance(int distance) {
        this.distance = distance;
    }

    public void validCheckOverDistance(int distance) {
        if (this.distance <= distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Distance minus(int distance) {
        return Distance.from(this.distance - distance);
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    public Distance plus(Distance distance) {
        return Distance.from(this.distance - distance.distance);
    }

    public int getDistance() {
        return distance;
    }
}
