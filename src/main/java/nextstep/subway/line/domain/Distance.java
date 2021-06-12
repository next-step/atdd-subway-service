package nextstep.subway.line.domain;

import static java.lang.String.format;

public class Distance {
    private static final int MINIMUM_DISTANCE = 0;

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validate(distance);

        this.distance = distance;
    }

    private void validate(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new IllegalArgumentException(format("최소 거리는 %d 이상이여야 합니다", MINIMUM_DISTANCE));
        }
    }
}
