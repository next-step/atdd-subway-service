package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidDistanceValueException;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Distance {
    @Transient
    public static final int MIN_DISTANCE_VALUE = 0;

    private int distance;

    protected Distance() {
    }

    public Distance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance < MIN_DISTANCE_VALUE) {
            throw new InvalidDistanceValueException("거리는 음수가 될 수 없습니다.");
        }
    }
}
