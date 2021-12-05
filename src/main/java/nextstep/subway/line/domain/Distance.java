package nextstep.subway.line.domain;

import nextstep.subway.line.exception.DistanceException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String DISTANCE_MIN_SIZE_MESSAGE = "구간의 거리는 0보다 커야 합니다.";
    private static final int DISTANCE_MIN_VALUE = 0;
    @Column(name = "distance")
    private int value;

    protected Distance() {

    }

    private Distance(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int value) {
        if (value <= DISTANCE_MIN_VALUE) {
            throw new DistanceException(DISTANCE_MIN_SIZE_MESSAGE);
        }
    }

    public static Distance of(int value) {
        return new Distance(value);
    }

    public Distance plus(Distance distance) {
        return Distance.of(this.value + distance.value);
    }

    public Distance minus(Distance distance) {
        return Distance.of(this.value - distance.value);
    }
}
