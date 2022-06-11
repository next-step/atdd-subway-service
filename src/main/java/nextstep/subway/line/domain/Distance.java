package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    public static final int MINIMUM_DISTANCE = 1;

    protected Distance() {
    }

    private Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public static Distance from(int distance) {
        return new Distance(distance);
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public void add(Distance distance) {
        this.distance += distance.getDistance();
        validateDistance(this.distance);
    }

    public void subtract(Distance distance) {
        this.distance -= distance.getDistance();
        validateDistance(this.distance);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Distance)) {
            return false;
        }
        return ((Distance)obj).getDistance() == distance;
    }
}
