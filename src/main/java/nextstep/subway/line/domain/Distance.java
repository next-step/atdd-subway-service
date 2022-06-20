package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String ERROR_MESSAGE_VALUE_LOWER_THAN_ZERO = "거리값은 0 이하일 수 없습니다.";
    private static final String ERROR_MESSAGE_VALUE_GREATER_THAN_CURRENT = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    @Column(name = "distance")
    private int distance;

    public Distance(int distance) {
        validatePositive(distance);
        this.distance = distance;
    }

    private void validatePositive(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_VALUE_LOWER_THAN_ZERO);
        }
    }

    public int getDistance() {
        return distance;
    }

    public void addDistance(Distance targetDistance) {
        validatePositive(targetDistance);
        this.distance += targetDistance.getDistance();
    }

    private void validatePositive(Distance distance) {
        if (distance.getDistance() <= 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_VALUE_LOWER_THAN_ZERO);
        }
    }

    public void subDistance(Distance targetDistance) {
        validateGreaterThan(targetDistance);
        this.distance += targetDistance.getDistance();
    }

    private void validateGreaterThan(Distance targetDistance) {
        if(this.distance <= targetDistance.getDistance()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_VALUE_GREATER_THAN_CURRENT);
        }
    }
}
