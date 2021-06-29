package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String INVALID_DISTANCE = "유효하지 않은 거리입니다.";
    private static final String NOT_ENOUGH_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private final int MINIMUM_DISTANCE = 0;

    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance of(int distance){
        return new Distance(distance);
    }

    private void validate(int distance) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(INVALID_DISTANCE);
        }
    }

    public Distance subtractDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException(NOT_ENOUGH_DISTANCE);
        }
        return new Distance(this.distance - newDistance);
    }

    public int toNumber() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Distance distance1 = (Distance)o;
        return MINIMUM_DISTANCE == distance1.MINIMUM_DISTANCE && distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(MINIMUM_DISTANCE, distance);
    }
}
