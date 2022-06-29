package nextstep.subway.line.domain.wrap;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class Distance {

    public static final int MIN = 0;
    public static final String INVALID_DISTANCE_ERROR = "구간 거리는 0 보다 큰 값을 입력하세요";
    public static final String INVALID_OPERATE_DISTANCE_ERROR = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        validateMinimumDistance(distance);
        this.distance = distance;
    }

    private void validateMinimumDistance(int distance) {
        if (distance <= MIN) {
            throw new IllegalArgumentException(INVALID_DISTANCE_ERROR);
        }
    }

    public void minusDistance(int distance) {
        validateOperateDistance(distance);
        this.distance = this.distance - distance;
    }

    private void validateOperateDistance(int targetDistance) {
        if (isOverOrEquals(targetDistance)) {
            throw new IllegalArgumentException(INVALID_OPERATE_DISTANCE_ERROR);
        }
    }

    private boolean isOverOrEquals(int targetDistance) {
        return distance <= targetDistance;
    }

    public void plusDistance(int distance) {
        this.distance = this.distance + distance;
    }

    public int getDistance() {
        return distance;
    }
}
