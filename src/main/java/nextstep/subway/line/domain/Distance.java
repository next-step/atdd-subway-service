package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    public static final String CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE = "기존 역 사이 길이보다 크거나 같은 구간은 추가할 수 없습니다.";

    @Column
    int distance;

    public Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    protected void minusDistance(Distance distance) {
        if (isShortEqualThan(distance)) {
            throw new IllegalArgumentException(CANNOT_ADD_SECTION_GREATER_THAN_OR_EQUAL_DISTANCE);
        }
        this.distance -= distance.get();
    }

    private boolean isShortEqualThan(Distance distance) {
        return this.distance <= distance.get();
    }

    protected void plusDistance(Distance distance) {
        this.distance += distance.get();
    }

    public int get() {
        return distance;
    }

    @Override
    public String toString() {
        return String.valueOf(distance);
    }
}
