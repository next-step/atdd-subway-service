package nextstep.subway.line.collection;

import nextstep.subway.line.domain.Section;

import javax.persistence.Embeddable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final String DISTANCE_ILLEGAL_EXCEPTION = "기존 역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private static final int MINIMUM_DISTANCE = 1;

    private final int distance;

    protected Distance() {
        this.distance = MINIMUM_DISTANCE;
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public Distance measureOldDistance(Distance oldDistance, boolean isConnect) {
        if (!isConnect) {
            oldDistance.isLongThanNewDistance(this.distance);
        }
        return new Distance(oldDistance.getDistanceOfNewSection(this.distance, isConnect));
    }

    private int getDistanceOfNewSection(int newDistance, boolean isConnect) {
        if (isConnect) {
            return this.distance + newDistance;
        }
        return this.distance - newDistance;
    }

    private void isLongThanNewDistance(int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalArgumentException(DISTANCE_ILLEGAL_EXCEPTION);
        }
    }

    public Map<Section, Integer> ofSectionMap(Section section) {
        Map<Section, Integer> sectionMap = new HashMap<>();
        sectionMap.put(section, distance);
        return sectionMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
