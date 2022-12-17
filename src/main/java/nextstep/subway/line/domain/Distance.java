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

    public Distance subtract(Distance distance) {
        return Distance.from(this.distance - distance.distance);
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public static Distance from(Integer distance) {
        return new Distance(distance);
    }

    public int value() {
        return distance;
    }

    public boolean isOverDistance(int distance) {
        return this.distance <= distance;
    }

}
