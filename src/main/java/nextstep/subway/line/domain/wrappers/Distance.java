package nextstep.subway.line.domain.wrappers;

import nextstep.subway.exception.BadDistanceException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private static final int MINIMUM_DISTANCE = 1;
    private static final String ZERO_OR_NEGATIVE_NUMBER_ERROR_MESSAGE = "구간 거리 값은 0보다 큰값만 입력 가능 합니다.";
    private static final String OUT_BOUND_DISTANCE_ERROR_MESSAGE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    private int distance;

    public Distance() {
    }

    public Distance(int distance) {
        checkValidZeroOrNegative(distance);
        this.distance = distance;
    }

    public Distance subtractDistance(Distance other) {
        int value = distance - other.distance;
        checkValidOutBoundDistance(value);
        return new Distance(value);
    }

    public Distance sumDistance(Distance other) {
        return new Distance(distance + other.distance);
    }

    private void checkValidZeroOrNegative(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new BadDistanceException(ZERO_OR_NEGATIVE_NUMBER_ERROR_MESSAGE);
        }
    }

    private void checkValidOutBoundDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new BadDistanceException(OUT_BOUND_DISTANCE_ERROR_MESSAGE);
        }
    }

    public int distance() {
        return distance;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Distance distance1 = (Distance) object;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
