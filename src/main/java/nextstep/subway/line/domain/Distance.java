package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final String ERROR_INVALID_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private static final String ERROR_MINUS_DISTANCE = "거리는 0보다 큰 값을 입력해주세요.";
    @Column
    private final int distance;

    public Distance() {
        this.distance = 0;
    }

    public Distance(int distance) {
        if (isMinusOrZero(distance)) {
            throw new IllegalArgumentException(ERROR_MINUS_DISTANCE);
        }
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public Distance minus(Distance minusDistance) {
        if (isSameOrFarther(minusDistance)) {
            throw new IllegalArgumentException(ERROR_INVALID_DISTANCE);
        }

        return new Distance(this.distance - minusDistance.distance);
    }

    public Distance plus(Distance plusDistance) {
        return new Distance(this.distance + plusDistance.distance);
    }

    private boolean isMinusOrZero(int distance) {
        return distance <= 0;
    }

    private boolean isSameOrFarther(Distance target) {
        return this.distance <= target.distance;
    }
}
