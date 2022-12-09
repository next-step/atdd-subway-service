package nextstep.subway.line.domain;

import nextstep.subway.common.exception.InvalidDataException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final String INVALID_DISTANCE_EXCEPTION = "거리는 1 보다 커야 합니다.";
    private static final String NEW_DISTANCE_SHOULD_BE_SMALLER_THAN_PREVIOUS_EXCEPTION = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    @Column(nullable = false)
    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        validateMinNumber(distance);
        this.distance = distance;
    }

    private void validateMinNumber(int distance) {
        if (distance < 1) {
            throw new InvalidDataException(INVALID_DISTANCE_EXCEPTION);
        }
    }

    public Distance add(Distance addDistance) {
        this.distance += addDistance.value();
        return this;
    }

    public void subtract(Distance minusDistance) {
        distance -= minusDistance.distance;
    }

    public void validateDistance(Distance newDistance) {
        if (distance <= newDistance.distance) {
            throw new InvalidDataException(NEW_DISTANCE_SHOULD_BE_SMALLER_THAN_PREVIOUS_EXCEPTION);
        }
    }

    public int value() {
        return this.distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Distance distance1 = (Distance) obj;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
