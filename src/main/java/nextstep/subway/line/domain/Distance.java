package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    public static final String ERROR_INVALID_DISTANCE = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    @Column
    private int distance;

    public Distance() {

    }

    public Distance(int distance) {
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

    private boolean isSameOrFarther(Distance target) {
        return this.distance <= target.distance;
    }
}
