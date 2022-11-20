package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import nextstep.subway.common.constant.ErrorCode;

@Embeddable
public class Distance {

    private static final int ZERO = 0;

    @Column(nullable = false)
    private int distance;

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
        if(distance <= ZERO) {
            throw new IllegalArgumentException(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
        }
    }

    public int value() {
        return distance;
    }
}
