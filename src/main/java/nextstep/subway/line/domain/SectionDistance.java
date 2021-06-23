package nextstep.subway.line.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SectionDistance extends DefaultWeightedEdge {
    private static final int SECTION_DISTANCE_MIN = 1;

    @Column
    private int distance;

    public SectionDistance() {
    }

    public SectionDistance(int distance) {
        verifyGreaterThanOrEqualToSectionDistanceMin(distance);
        this.distance = distance;
    }

    private static void verifyGreaterThanOrEqualToSectionDistanceMin(int distance) {
        if (distance < SECTION_DISTANCE_MIN) {
            throw new IllegalArgumentException("구간 길이는 1 이상이어야 합니다.");
        }
    }

    public void updateDistance(int newDistance) {
        verifyGreaterThanOrEqualToSectionDistanceMin(newDistance);
        verifyGreaterThanOrEqualToCurrentDistance(newDistance);
        distance -= newDistance;
    }

    private void verifyGreaterThanOrEqualToCurrentDistance(int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionDistance distance1 = (SectionDistance) o;
        return getDistance() == distance1.getDistance();
    }
}
