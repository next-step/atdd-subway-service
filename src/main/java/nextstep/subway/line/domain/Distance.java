package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final String ERROR_MESSAGE_NOT_NULL_DISTANCE = "거리는 필수입니다.";
    private static final String ERROR_MESSAGE_GRATER_THAN_ZERO_DISTANCE = "거리는 0보다 커야합니다.";
    public static final String ERROR_MESSAGE_VALID_SECTION_DISTANCE = "기존 노선의 거리보다 작거나 같을 수 없습니다.";
    private static final int INVALID_DISTANCE_CRITERION = 0;

    @Column(nullable = false)
    private Integer distance;

    protected Distance() {
    }

    private Distance(Integer distance) {
        validDistance(distance);
        this.distance = distance;
    }

    public static Distance from(Integer distance) {
        return new Distance(distance);
    }

    private void validDistance(Integer distance) {
        if (Objects.isNull(distance)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_NULL_DISTANCE);
        }

        if (distance <= INVALID_DISTANCE_CRITERION) {
            throw new IllegalArgumentException(ERROR_MESSAGE_GRATER_THAN_ZERO_DISTANCE);
        }
    }

    public Distance add(Distance distance) {
        return Distance.from(this.distance + distance.distance);
    }

    public Distance subtract(Distance distance) {
        int subtractDistance = this.distance - distance.distance;
        validSectionDistance(subtractDistance);
        return Distance.from(subtractDistance);
    }

    private static void validSectionDistance(int subtract) {
        if (subtract <= INVALID_DISTANCE_CRITERION) {
            throw new IllegalArgumentException(ERROR_MESSAGE_VALID_SECTION_DISTANCE);
        }
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance1 = (Distance) o;
        return Objects.equals(distance, distance1.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
