package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야 합니다.");
        }
    }

    public Distance minus(Distance distance) {
        try {
            return Distance.from(this.distance - distance.distance);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("기존 노선의 거리보다 작거나 같을 수 없습니다.");
        }
    }

    public Distance plus(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public static Distance from(Integer distance) {
        return new Distance(distance);
    }

}
