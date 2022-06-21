package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;

public class Distance {
    public static int MIN_VALUE = 1;

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    public Distance plus(int distance) {
        return new Distance(this.distance + distance);
    }

    public Distance minus(int distance) {
        return new Distance(this.distance - distance);
    }

    private void validateDistance(int distance) {
        if (distance < MIN_VALUE) {
            throw new BadRequestException("거리는 1보다 작을 수 없습니다.");
        }
    }

    public int toInt() {
        return distance;
    }
}
