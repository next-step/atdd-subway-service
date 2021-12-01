package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : Distance
 * author : haedoang
 * date : 2021-12-01
 * description :
 */
@Embeddable
public class Distance {
    @Transient
    public static final int MIN_DISTANCE = 1;

    private Integer distance;

    protected Distance() {
    }

    private Distance(int distance) {
        validate(distance);
        this.distance = distance;
    }

    public static Distance valueOf(DistanceType type, Distance src, Distance target) {
        if (type.isPlus()) {
            return Distance.of(src.distance + target.distance);
        }

        if (type.isMinus()) {
            return Distance.of(src.distance - target.distance);
        }

        return null;
    }

    public static Distance of(int distance) {
        return new Distance(distance);
    }

    private void validate(int distance) {
        if (distance < MIN_DISTANCE) throw new IllegalArgumentException("거리는 1보다 작을 수 없습니다.");
    }

    public boolean isLessThanOrEqualTo(Distance newDistance) {
        return distance <= newDistance.distance;
    }
}
